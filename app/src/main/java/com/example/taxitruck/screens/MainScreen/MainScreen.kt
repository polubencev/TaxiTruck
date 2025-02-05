package com.example.taxitruck.screens.MainScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.taxitruck.R
import com.example.taxitruck.data.Book
import com.example.taxitruck.screens.MainScreen.bottom_menu.BottomMenu
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    email:String,
    onAdminClick:() -> Unit,
    onBookEditClick:(Book) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val bookListState = remember { mutableStateOf(emptyList<Book>())}
    val downloadImageState = remember { mutableStateOf<Boolean>(false) }
    val isAdminState = remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        getAllBooks(db,{listBook ->
            bookListState.value = listBook

        },
            onDownloadState = { state ->
                downloadImageState.value = state
            })



        Log.d("MyTag", "downloadState = " + downloadImageState.value.toString())
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(email)
                DrawerBody(onAdmin = {isAdmin ->
                    isAdminState.value = isAdmin
                }) {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    onAdminClick()
                } //Тело дравера
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize().navigationBarsPadding(),
            bottomBar = { BottomMenu() }
        ) {paddingValues->

                Image(
                    painter = painterResource(R.drawable.drawer_background), "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            if(!downloadImageState.value) {                                    //******Кружок загрузки*******//
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                }
            }else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(11.dp),

                ) {

                    items(bookListState.value) { book ->
                        BookListItemUI(book, isAdminState.value){book->
                            onBookEditClick(book)
                        }

                    }
                }
            }
            /*******/
        }
    }
}

private fun getAllBooks(
    db: FirebaseFirestore,
    onBooks: (List<Book>)->Unit,
    onDownloadState: (Boolean) -> Unit,
) {
    db.collection("books").get().addOnSuccessListener(){task->
    onBooks(task.toObjects(Book::class.java))
        onDownloadState(true)

    }

}

package com.example.taxitruck.screens.MainScreen

import android.icu.text.UnicodeSetIterator
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taxitruck.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun DrawerBody(
    onAdmin:(Boolean)->Unit,
    onAdminClick:()->Unit
) {
    val isAdminState = remember{ mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isAdmin {it->
            isAdminState.value = it         //Проверка на то,что администратор
            onAdmin(isAdminState.value)
        }
    }

    val categoriesList = listOf(
        "Favorites",
        "Fantasy",
        "Drama",
        "Bestsellers"
    )
    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black, shape = RoundedCornerShape(2.dp)).background(Color.Gray)
        ) {
            Image(
                painter = painterResource(R.drawable.drawer_background),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                alpha = 0.8f
            )
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                Text(
                    "Categories",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    thickness = DividerDefaults.Thickness,
                    Color.Blue
                )
                Spacer(Modifier.height(5.dp))
                LazyColumn(
                    Modifier.fillMaxWidth()
                ) {
                    items(categoriesList) { item ->

                        Column(Modifier.fillMaxWidth().clickable {},
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = item,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.wrapContentWidth()
                            )

                            Spacer(Modifier.height(5.dp))
                            HorizontalDivider(
                                Modifier
                                    .fillMaxWidth()
                                    .height(2.dp),
                                thickness = DividerDefaults.Thickness,
                                Color.LightGray
                            )

                        }


                    }
                }
                if(isAdminState.value) Button(onClick = {
                    onAdminClick()

                }) {
                    Text("Админ-панель")
                }


            }
        }
    }
}

fun isAdmin(onAdmin:(Boolean) ->Unit){
    val uid = Firebase.auth.currentUser!!.uid
    Firebase.firestore.collection("admin").document(uid).get().addOnSuccessListener {
        onAdmin(it.get("isAdmin") as Boolean)
    }

}
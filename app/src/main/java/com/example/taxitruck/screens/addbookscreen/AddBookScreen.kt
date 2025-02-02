package com.example.taxitruck.screens.addbookscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taxitruck.R
import com.example.taxitruck.screens.LoginButton
import com.example.taxitruck.screens.RoundedCornerTextField
import coil3.compose.rememberAsyncImagePainter
import com.example.taxitruck.data.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@Preview(showBackground = true)
@Composable
fun AddBookScreen() {
    var selectedCategory: String = ""
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val imageURI = remember { mutableStateOf<Uri?>(null) }
    val firestore = remember { Firebase.firestore }
    val storage = remember { Firebase.storage }
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageURI.value = uri

    }
    Image(
        painter = rememberAsyncImagePainter(model = imageURI.value),
        contentDescription = "background",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        alpha = 1.0f
    )
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(R.drawable.logo_add_truck_track),
            contentDescription = "", contentScale = ContentScale.Crop
        )
        Text(
            text = "Добавить новую книгу в базу",
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        HorizontalDivider(
            modifier = Modifier.height(20.dp),
            color = Color.Black
        )

        RoundedCornerTextField(
            1,
            true,
            title.value,
            "Название",

            ) {
            title.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))
        RoundedDropDownMenu { selectedItem ->
            selectedCategory = selectedItem
        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            5,
            false,
            description.value,
            "Описание книги"
        ) {
            description.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            1,
            false,
            price.value,
            "Цена"
        ) {
            price.value = it
        }
        Spacer(modifier = Modifier.height(15.dp))
        //**********************************************************//
        LoginButton("Выбрать обложку") {
            imageLauncher.launch("image/*")

        }
        Spacer(modifier = Modifier.height(10.dp))


        LoginButton("Сохранить") {
            saveBookImage(uri = imageURI.value!!,
                storage,
                firestore,
                Book(name = title.value,
                    description = description.value,
                    price = price.value,
                    category = selectedCategory,
                    ),
                onSaved = {},
                onError = {})
        }
        //*****************************************************************************************//

    }
}

//*********************************************************************//
private fun saveBookImage(
    uri: Uri,
    storage: FirebaseStorage,
    fireStore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val timeStamp = System.currentTimeMillis()
    var storageRef = storage.reference.child("book/images")
        .child("{$timeStamp}.png")
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener { url ->
            saveBookToFireStore(fireStore, url.toString(), book,
                onSaved = {onSaved()},
                onError = {onError()})

        }
    }
}

private fun saveBookToFireStore(
    fireStore: FirebaseFirestore,
    uri: String,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val db = fireStore.collection("books")
    val key = db.document().id
    db.document(key).set(book.copy(key = key, imageUrl = uri)).addOnSuccessListener {
        onSaved()
    }
        .addOnFailureListener {
            onError()
        }

}
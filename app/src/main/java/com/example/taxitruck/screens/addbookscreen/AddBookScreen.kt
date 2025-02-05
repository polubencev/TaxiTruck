package com.example.taxitruck.screens.addbookscreen

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taxitruck.R
import com.example.taxitruck.data.Book
import com.example.taxitruck.screens.LoginButton
import com.example.taxitruck.screens.RoundedCornerTextField
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@Preview(showBackground = true)
@Composable
fun AddBookScreen(
    onSaved: () -> Unit= {}
) {

    val context = LocalContext.current
    val cr = context.contentResolver
    var selectedCategory = remember{
        mutableStateOf("")}
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
        painter = painterResource(R.drawable.background),
        //painter = rememberAsyncImagePainter(model = imageURI.value),
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
            painter = painterResource(R.drawable.logo_card_orders),
            contentDescription = "", contentScale = ContentScale.Crop
        )
        Text(
            text = "Добавить новый заказ",
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
            selectedCategory.value = selectedItem
            Log.d("MyTag", selectedItem)
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

//***********************************SAVE BUTTON****************************************************//
        LoginButton("Сохранить") {
            //Save image book to Firestore
            val book = Book(
                name = title.value,
                description = description.value,
                price = price.value,
                category = selectedCategory.value,
                imageUrl = imageToBase64(imageURI.value!!,cr)
            )
            saveBookToFireStore(firestore,book,
                onSaved = {
                Toast.makeText(context, "Книга {${book.name}} успешно сохранена!",Toast.LENGTH_SHORT).show()
                title.value = ""
                description.value = ""
                price.value = ""
                selectedCategory.value = ""
                imageURI.value = null
                onSaved() //Это для возвращения на 1 экран назад,вызывается в MainActivity в навигации
            }, onError = {error->
                Toast.makeText(context, "Ошибка сохранения книги!:" + error,Toast.LENGTH_SHORT).show()
            })
        }
        //*****************************************************************************************//

    }
}
/**************************SAVE IMAGE BASE64*******************************************************/
private fun imageToBase64(uri: Uri, contentResolver: ContentResolver):String{
    val inputStream = contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes()
    return bytes?.let{
        android.util.Base64.encodeToString(it,android.util.Base64.DEFAULT)
    }?: ""
}

//*********************************************************************//
/*private fun saveBookImage(
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
}*/

private fun saveBookToFireStore(
    fireStore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: (String) -> Unit
) {
    val db = fireStore.collection("books")
    val key = db.document().id
    db.document(key).set(book.copy(key = key)).addOnSuccessListener {
        onSaved()
    }
        .addOnFailureListener {error->
            onError(error.message!!)
        }

}
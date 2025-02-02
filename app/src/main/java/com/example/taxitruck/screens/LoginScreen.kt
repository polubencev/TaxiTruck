package com.example.taxitruck.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taxitruck.R
import com.example.taxitruck.screens.MainScreen.data.MainScreenObject
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(
    onNavigateToMainScreen:(MainScreenObject)->Unit
) {
    Image(
        painter = painterResource(R.drawable.background), contentDescription = "background",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    val auth = remember { Firebase.auth }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val errorState = remember { mutableStateOf("") }
    val inSystemState = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(auth.currentUser?.email?.isNotBlank() == true) {
        Log.d("MyTag", inSystemState.value.toString())
            Text(text = ("Вы в системе как: " + auth.currentUser?.email!!),
                fontSize = 10.sp,
                color =  Color.White)
        }else{
            RoundedCornerTextField(1,
                true,
                emailState.value,
                "Email"
            ) {
                emailState.value = it
            }

            Spacer(modifier = Modifier.height(10.dp))
            RoundedCornerTextField(
                1,
                true,
                passwordState.value,
                "Password"
            ) {
                passwordState.value = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            //**********************************************************//
            LoginButton("Регистрация") {
                signUp(auth,
                    emailState.value,
                    passwordState.value,
                    onSignUpSuccess = { navData ->
                        onNavigateToMainScreen(navData)
                        errorState.value = "Регистрация прошла успешно!"
                    },
                    onSignUpFailure = { error ->
                        errorState.value = error

                    }
                )
            }
        }
        //*****************************************************************************************//
        Spacer(modifier = Modifier.height(10.dp))
        LoginButton("Вход") {
            signIn(auth,
                emailState.value,
                passwordState.value,
                onSignInSuccess = {navData->
                    onNavigateToMainScreen(navData)
                    errorState.value = "Успешный вход"
                },
                onSignInFailure = {error ->
                    errorState.value = error

                }
                )

        }
        //******//
        if(auth.currentUser!=null){
            LoginButton("Выход") {
                auth.signOut()
                inSystemState.value = false
            }
        }

        Text(text = errorState.value,
            fontSize = 8.sp,
            color =  Color.Red)

    }
}
//*************Регистрация*****************////////
private fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess:  (MainScreenObject) -> Unit,
    onSignUpFailure:  (String) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure("Заполните все поля")
        return
    }
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onSignUpSuccess(
                MainScreenObject(
                    uid = task.result.user?.uid ?: "",
                    email = task.result.user?.email?: ""
                )
            )
            Log.d("MyLog", "Sign up is successed!")
        }
    }.addOnFailureListener() {
        onSignUpFailure(it.message ?: "<-Unknown SignUp Error!->")
    }
}
//===================ВХОД================================//
private fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignInSuccess:(MainScreenObject) ->Unit,
    onSignInFailure:(String)->Unit,
) {
    if ((email.isBlank() || password.isBlank()) && auth.currentUser == null) {
        onSignInFailure("Заполните все поля!")
        return
    } else if (auth.currentUser != null) {
        onSignInSuccess(
            MainScreenObject(
                uid = auth.currentUser?.uid ?: "",
                email = auth.currentUser?.email ?: ""
            )
        )
    } else {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                onSignInSuccess(
                    MainScreenObject(
                        uid = task.result.user?.uid ?: "",
                        email = task.result.user?.email ?: ""
                    )
                )

            }
        }.addOnFailureListener() { error ->
            onSignInFailure(error.message ?: "Error")

        }
    }
}


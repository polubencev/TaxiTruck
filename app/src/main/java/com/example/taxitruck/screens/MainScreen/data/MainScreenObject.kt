package com.example.taxitruck.screens.MainScreen.data

import kotlinx.serialization.Serializable

@Serializable                   //Помечаем так для навигации
data class MainScreenObject(
    val uid: String = "",
    val email: String = ""
)

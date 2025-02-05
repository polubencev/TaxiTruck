package com.example.taxitruck.screens.addbookscreen.data

import kotlinx.serialization.Serializable

@Serializable
data class AddBookScreenObject(
    val key: String="",
    val name: String="",
    val description: String="",
    val price: String="",
    val category: String="",
    val imageUrl: String=""
)
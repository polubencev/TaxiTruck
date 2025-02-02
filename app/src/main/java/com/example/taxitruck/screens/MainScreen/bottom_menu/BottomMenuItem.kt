package com.example.taxitruck.screens.MainScreen.bottom_menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import com.example.taxitruck.R

sealed class BottomMenuItem(
    val route: String,
    val title:String,
    val iconId:Int
)
{
    object Home: BottomMenuItem(
        route = "home",
        title = "Home",
        iconId = R.drawable.home
    )

    object Favs: BottomMenuItem(
        route = "favs",
        title = "Favorites",
        iconId = R.drawable.favs
    )

    object Settings: BottomMenuItem(
        route = "settings",
        title = "Settings",
        iconId = R.drawable.settings
    )

}
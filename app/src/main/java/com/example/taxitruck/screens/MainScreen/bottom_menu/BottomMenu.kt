package com.example.taxitruck.screens.MainScreen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource

@Composable
fun BottomMenu() {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favs,
        BottomMenuItem.Settings
    )
    val selectedItems = remember { mutableStateOf("Home") }
    NavigationBar {

        items.forEach() { item ->
            NavigationBarItem(
                selected = selectedItems.value == item.title,
                onClick = {selectedItems.value = item.title},
                icon = {
                    Icon(
                        painterResource(id = item.iconId),
                        contentDescription = ""
                    )
                }, label = { Text(item.title) }
            )
        }
    }

}
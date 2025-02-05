package com.example.taxitruck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.taxitruck.screens.LoginScreen
import com.example.taxitruck.screens.MainScreen.MainScreen
import com.example.taxitruck.screens.MainScreen.data.LoginScreenObject
import com.example.taxitruck.screens.MainScreen.data.MainScreenObject
import com.example.taxitruck.screens.addbookscreen.AddBookScreen
import com.example.taxitruck.screens.addbookscreen.data.AddBookScreenObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController= navController, startDestination = LoginScreenObject) {
                composable<LoginScreenObject> {
                    LoginScreen { navData ->
                        navController.navigate(navData)
                    }
                }

                composable<MainScreenObject> {navEntry->
                    val navData = navEntry.toRoute<MainScreenObject>()

                    MainScreen(navData.email,
                        onAdminClick = {
                        navController.navigate(AddBookScreenObject)
                    },
                        onBookEditClick = {book->
                        navController.navigate(AddBookScreenObject(
                            key = book.key,
                            name = book.name,
                            description = book.description,
                            price = book.price,
                            imageUrl = book.imageUrl,
                            category = book.category
                        ))
                    }
                    );
                    {

                        navController.navigate(AddBookScreenObject())
                    }
                }
                composable<AddBookScreenObject> {navEntry->
                    val navData = navEntry.toRoute<AddBookScreenObject>()
                    AddBookScreen(navData)
                }
            }
        }
    }
}



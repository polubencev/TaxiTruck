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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController= navController, startDestination = LoginScreenObject){
                composable<LoginScreenObject> {
                    LoginScreen{navData->
                        navController.navigate(navData)

                    }
                }

                composable<MainScreenObject> {navEntry->
                    val navData = navEntry.toRoute<MainScreenObject>()

                    MainScreen(navData.email)
                }
            }
        }
    }
}



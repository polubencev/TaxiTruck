package com.example.taxitruck.screens.MainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taxitruck.R

@Composable
fun DrawerHeader(email: String) {
    Column(
        modifier =Modifier.fillMaxWidth().height(170.dp)
            .background(colorResource(R.color.drawer_header))
        , horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center
    )
    {
        Image(painter = painterResource(R.drawable.logo_login_screen), contentDescription = "logo",
            modifier = Modifier.size(90.dp))

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "TruckTaxi",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(modifier = Modifier.width(100.dp))
        Text(
            text = email,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }

    /***/
}
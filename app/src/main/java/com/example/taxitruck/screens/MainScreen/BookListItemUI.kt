package com.example.taxitruck.screens.MainScreen

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.taxitruck.R
import com.example.taxitruck.data.Book

@Composable
fun BookListItemUI(book: Book) {

    Card(modifier=Modifier.fillMaxWidth(0.8f).border(1.dp,Color.LightGray,RoundedCornerShape(10.dp))
        .padding(start = 0.dp, end = 0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.card_content_color))) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start) {

                /**************Тут декодирование изображения по base64*****************/
                val base64Image = Base64.decode(book.imageUrl, Base64.DEFAULT )
                val bitmapImageDecode = BitmapFactory.decodeByteArray(base64Image,0,base64Image.size)

                AsyncImage(
                    model = R.drawable.logo_card_orders, contentDescription = "",
                    modifier = Modifier.size(35.dp).padding(start = 0.dp)
                        .weight(0.2f),
                    contentScale = ContentScale.Fit
                )
                //Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier.padding(start = 10.dp).weight(0.7f).fillMaxWidth(0.7f),
                    text = book.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                book.description,
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 7.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            //***************PRICE**************************//
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center) {
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp, start = 5.dp),
                        text = book.price + "р.",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
                Column(modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        /****************/
                        Button(onClick = {

                        }, colors = ButtonDefaults.buttonColors(colorResource(R.color.accept_order_button_color)),
                            border = BorderStroke(1.dp, Color.Yellow)
                        ) {
                            Text("?")
                        }
                        /****************/
                        Button(onClick = {

                        }, colors = ButtonDefaults.buttonColors(colorResource(R.color.accept_order_button_color)),
                            border = BorderStroke(1.dp, Color.Yellow)
                        ) {
                            Text("+")
                        }
                    }
                }
            }



        }
    }
}
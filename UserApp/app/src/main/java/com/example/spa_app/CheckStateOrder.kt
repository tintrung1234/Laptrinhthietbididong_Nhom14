package com.example.spa_app

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CheckStateOrder(state: Int){
    var text: String
    var color: Int
    when(state){
        0 -> {
            text = "Đã đặt"
            color = 0xFFDF2D2D.toInt()
        }
        else -> {
            text = "Đã hoàn thành"
            color = 0xFF09E932.toInt()
        }
    }
    Text(
        text = text,
        color = Color(color),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CheckPayment(state: Boolean){
    var text: String
    var color: Int
    if(state){
        text = "Đã thanh toán"
        color = 0x80000000.toInt()
    }
    else{
        text = "Chưa thanh toán"
        color = 0xFFDF2D2D.toInt()
    }
    Text(
        text = text,
        color = Color(color),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End // Căn văn bản bên phải
    )
}

fun calculateAverageRating(rateOld: Int, rateNew: Int, rateCount: Int): Int{
    val rateAvg = (rateOld * rateCount + rateNew)/(rateCount+1)
    return rateAvg
}
package com.example.spa_app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CheckStateOrder(state: Int){
    var text: String
    var color: Int
    when(state){
        1 -> {
            text = "Chờ xác nhận"
            color = 0xFFDF2D2D.toInt()
        }
        2 -> {
            text = "Đã xác nhận"
            color = 0xFF09E932.toInt()
        }
        3 -> {
            text = "Hoàn thành"
            color = 0xFFFFDC5D.toInt()
        }
        else -> {
            text = "Không xác định"
            color = 0xFFFFDC5D.toInt()
        }
    }
    Text(
        text = text,
        color = Color(color),
        fontWeight = FontWeight.Bold
    )
}
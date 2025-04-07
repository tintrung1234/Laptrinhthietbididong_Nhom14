package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun topLayout(title: String, onclick: () -> Unit){
    Row(
        modifier = Modifier.padding(bottom = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = {onclick}) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back", // Mô tả để hỗ trợ truy cập
                tint = Color.Black // Màu của icon
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(70.dp) // Tuỳ chỉnh kích thước
        )
    }
}
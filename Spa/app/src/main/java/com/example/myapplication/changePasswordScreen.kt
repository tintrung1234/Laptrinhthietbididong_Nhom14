package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun changePasswordScreen() {
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ){
        topLayout("Đổi mật khẩu", {})
        inputPassSection()
    }
}

@Composable
fun inputPassSection(){
    Column() {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo), // Ảnh từ drawable
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp) // Đặt kích thước ảnh
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .offset(x = 55.dp, y = 45.dp) // Di chuyển toàn bộ IconButton
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera), // Ảnh từ drawable
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(27.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                        .background(Color.White)
                )
            }

        }
        Text(
            text = "Xin chào ABC!",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        var editInfor by remember { mutableStateOf(true) }
        inforItem(Icons.Default.Lock,"Mật khẩu",true, editInfor, "password")
        inforItem(Icons.Default.Lock,"Nhập lại mật khẩu",true, editInfor, "password")

    }
}


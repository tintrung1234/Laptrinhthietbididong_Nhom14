package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun forgetPasswordScreen(){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 20.dp)
    ) {
        topLayout("Quên mật khẩu", {})
        Spacer(modifier = Modifier.height(70.dp))
        emailSection()
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = "Thời gian còn lại 0:30",
            fontSize = 16.sp,
            color = Color(0x80000000),
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(13.dp))
        codeComfirmSection()
        Spacer(modifier = Modifier.height(80.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD6D183)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 7.dp,
                pressedElevation = 12.dp
            )
        ) {
            Text(
                text = "Tiếp tục",
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun emailSection(){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Email",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            placeholder = { Text("abc@gmail.com", color = Color(0xB3000000))},
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(0xA6D9D9D9), // Màu nền khi không focus
                ),
            trailingIcon = {
                Text(
                    text = "Gửi",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                    )
            }
        )
    }
}

@Composable
fun codeComfirmSection(){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.VpnKey,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Mã xác nhận",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            placeholder = { Text("123xxx") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(0xA6D9D9D9), // Màu nền khi không focus
            ),
        )
    }
}
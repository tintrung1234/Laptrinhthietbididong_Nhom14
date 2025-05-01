package com.example.spa_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spa_app.TopLayout


@Composable
fun NotifyScreen(navController: NavController){
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ){
        TopLayout("Thông báo", { navController.navigate("TrangChu") })
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(20){
                i -> notiItem()
            }
        }
    }
}

@Composable
fun notiItem(){
    Column(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) // Bo góc trước
            .background(Color(android.graphics.Color.parseColor("#E0E0E0")))
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Column(modifier = Modifier.height(60.dp)) {
                Text(
                    text = "Bạn có lịch hẹn vào ngày 5/3/2025 ",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Row{
                    Text(
                        text ="Bây giờ",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = Color(0x80000000),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {},
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .height(20.dp)
                    ) {
                        Text(
                            text = "Xem chi tiết >>",
                            textDecoration = TextDecoration.Underline,
                            color = Color(0x80000000),
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(bottom = 10.dp))
}

package com.example.spa_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

//@Preview(showBackground = true)
@Composable
fun HistoryScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ){
        TopLayout("Lịch sử", {navController.navigate("TrangChu") })
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFD9D9D9)
            ),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
            ) {
                item { itemCardHistory(1, "01-03-2025",1, navController) }
                item { itemCardHistory(2, "02-04-2025",2, navController) }
                item { itemCardHistory(3, "03-05-2025",3, navController) }
                item { itemCardHistory(4, "04-06-2025",2, navController) }

            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun itemCardHistory(testLoop: Int, date: String, state: Int, navController: NavController){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),

        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Text(
                    text = date,
                    color = Color(0x801E1E1E),
                )
                CheckStateOrder(state)
            }
            Column(
                modifier = Modifier.fillMaxWidth()
                ) {
                for (i in 1..testLoop){
                    itemServiceHistory(R.drawable.khuyen_mai1,"COMBO 4 CHĂM SÓC DA LẤY NHÂN MỤN 88K", 88000f, 400000f, state, navController)
                }
            }
            Text(
                text = "Tổng tiền: 158.000đ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End // Căn văn bản bên phải
            )
            Text(
                text = "(Đã thanh toán)",
                color = Color(0x80000000),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End // Căn văn bản bên phải
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun itemServiceHistory(img: Int, name: String, costSale: Float, cost: Float, state: Int, navController: NavController){
    Row (
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 30.dp)
            .clickable(onClick = {
                navController.navigate( if (state==3) "DanhGia" else "ChiTietLichHen")
            }),
    ) {
        Image(
            painter = painterResource(id = img),
            contentDescription ="",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = name,
                fontSize = 13.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = formatCost(cost),
                    style = TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = formatCost(costSale),
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }
        }
    }
}

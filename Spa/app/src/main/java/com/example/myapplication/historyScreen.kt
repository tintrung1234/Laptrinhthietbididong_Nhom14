package com.example.myapplication

import android.text.Layout.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Preview(showBackground = true)
@Composable
fun historyScreen(){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ){
        topLayout("Lịch sử", {})
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFD9D9D9)
            ),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
            ) {
                item { itemCardHistory(1, "01-03-2025",1) }
                item { itemCardHistory(2, "02-04-2025",2) }
                item { itemCardHistory(3, "03-05-2025",3) }
                item { itemCardHistory(4, "04-06-2025",2) }

            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun itemCardHistory(testLoop: Int, date: String, state: Int){
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
                checkStateOrder(state)
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                ) {
                for (i in 1..testLoop){
                    itemServiceHistory(R.drawable.dichvu1,"COMBO 4 CHĂM SÓC DA LẤY NHÂN MỤN 88K", 88000f, 400000f)
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
fun itemServiceHistory(img: Int, name: String, costSale: Float, cost: Float){
    Row (
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 30.dp)
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

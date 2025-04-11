package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview (showBackground = true)
@Composable
fun serviceScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        Box(
            modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp))
        { topLayout("Dịch vụ",{})}
        Card(
            modifier = Modifier.fillMaxSize()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    clip = false // để không cắt nội dung
                ),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Đặt elevation = 0 để tránh đổ bóng mặc định,
        ) {
            CustomTopAppBar()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(20.dp)
            ) {
                item { itemCardService(1, "Bán chạy nhất", R.drawable.ic_hot) }
                item { itemCardService(2, "Ưu đãi", R.drawable.ic_sale) }
                item { itemCardService(3, "Các gói khác",-1) }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconWithText(R.drawable.ic_spa, "Spa", true)
            IconWithText(R.drawable.ic_nail, "Nail", false)
            IconWithText(R.drawable.ic_massage, "Massage", false)
            IconWithText(R.drawable.ic_mypham, "Mỹ phẩm", false)
            IconWithText(R.drawable.ic_skincare, "Chăm sóc da", false)
        }
    }
}


@Composable
fun IconWithText(icon: Int, title: String, isSelection: Boolean){
    Column(
        modifier = Modifier.padding(top = 15.dp)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(Color(if (isSelection) 0x80D9D9D9 else 0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton (
            onClick = { /* hành động khi click */ },
            modifier = Modifier
                .padding(7.dp)
                .size(48.dp) // kích thước tổng thể nút
                .clip(CircleShape) // bo tròn
                .background(Color(0xD9DBC37C)) // màu nền tùy ý (optional)
        ){
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = Color.Unspecified,
                )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
fun itemCardService(testLoop: Int, title: String, img: Int){
    Card(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0x4C1E1E1E),
                shape = RoundedCornerShape(15.dp) // phải giống shape của Card
            ),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),

            ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(5.dp))
                if (img != -1)
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = ""
                    )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                for (i in 1..testLoop){
                    itemDisplayService(R.drawable.dichvu1,"COMBO 4 CHĂM SÓC DA LẤY NHÂN MỤN 88K", 3107,4.8f,88000f, 400000f)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun itemDisplayService(img: Int, name: String, quantity: Int, star: Float,costSale: Float, cost: Float){
    Row (
        modifier = Modifier.fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Image(
            painter = painterResource(id = img),
            contentDescription ="",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = name,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "",
                    tint = Color(0xFF818181),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = " Đã bán: ${quantity}",
                    color = Color(0xFF818181)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Đánh giá: ${star}",
                    color = Color(0xFF818181)
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = Color(0xFFFFDC5D),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatCost(cost),
                    style = TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = formatCost(costSale),
                    fontSize = 22.sp,
                    color = Color.Red
                )
            }
        }
    }
}
package com.example.ownerscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.ui.text.style.TextDecoration
import java.time.format.TextStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen()
        }
    }
}


@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            HeaderSection()


            Spacer(modifier = Modifier.height(40.dp))

            CategorySection()

            Spacer(modifier = Modifier.height(10.dp))

            PromotionsSection()

            Spacer(modifier = Modifier.height(10.dp))

            StaffInfoSection()
        }

        MenuButtonsSection(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 200.dp))

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BottomNavigationBar()
        }
    }
}


@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(top = 30.dp)
    ) {
        // Ảnh nền banner
        Image(
            painter = painterResource(id = R.drawable.banner1),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Lớp overlay mờ
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(top = 12.dp)
        )

        // Nội dung bên trong banner
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Thanh tìm kiếm
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(32.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tìm kiếm", color = Color.Gray)
                }
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Bell",
                    tint = Color(0xFFD4AF37) // vàng
                )
            }


        }
    }
}


@Composable
fun MenuButtonsSection(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()


    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),

            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                Pair(Icons.Default.DateRange, "Lịch hẹn"),
                Pair(R.drawable.lienhe_icon, "Liên hệ"),
                Pair(Icons.Default.Star, "Đánh giá"),
                Pair(R.drawable.voucher_icon, "Mã giảm giá")
            )

            items.forEach { (icon, label) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEADFA2)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (icon) {
                            is ImageVector -> Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp) // chỉnh đồng nhất kích thước
                            )
                            is Int -> Icon(
                                painter = painterResource(id = icon),
                                contentDescription = label,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp) // chỉnh đồng nhất kích thước
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = label, fontSize = 12.sp)
                }
            }
        }
    }
}



@Composable
fun CategorySection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val categories = listOf(
            Pair(R.drawable.icon_spa, "Spa"),
            Pair(R.drawable.nail_icon, "Nail"),
            Pair(R.drawable.massage, "Massage"),
            Pair(R.drawable.comestic, "Mỹ Phẩm"),
            Pair(R.drawable.skincare, "Chăm Sóc Da")
        )

        categories.forEach { category ->
            val icon = category.first
            val label = category.second

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEADFA2)), // màu vàng nhẹ
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = label,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp) // chỉnh đồng nhất kích thước
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(label, fontSize = 12.sp)
            }
        }
    }
}



@Composable
fun PromotionsSection() {
    val promotions = listOf(
        Triple(R.drawable.prom1, "COMBO 4 CHĂM SÓC DA LẤY NHÂN MỤN 88K", Pair("88.000đ", "400.000đ")),
        Triple(R.drawable.prom2, "MASSAGE XUA TAN NHỨC MỎI", Pair("70.000đ", "360.000đ")),
        Triple(R.drawable.prom3, "CHĂM SÓC DA 7 BƯỚC CHUYÊN SÂU", Pair("249.000đ", "700.000đ"))
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "KHUYẾN MÃI HOT 🔥",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            promotions.forEach { (imageRes, title, prices) ->
                Card(
                    modifier = Modifier
                        .width(160.dp)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = prices.first,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                            Text(
                                text = prices.second,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun StaffInfoSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Đội ngũ nhân viên chuyên nghiệp", fontWeight = FontWeight.Bold)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bacsi), // ảnh bác sĩ
                contentDescription = "Bác sĩ da liễu",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Bác sĩ chuyên khoa da liễu", fontWeight = FontWeight.Medium)
                Text("10 năm kinh nghiệm", fontSize = 12.sp)
                Row {
                    repeat(5) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFFEADFA2), // màu nền bar
        contentColor = Color.Black,
    ) {
        val items = listOf(
            Pair<Any, String>(Icons.Default.Home, "Trang chủ"),
            Pair<Any, String>(R.drawable.history_icon, "Lịch sử"),
            Pair<Any, String>(R.drawable.dichvu_icon, "Chỉnh sửa dịch vụ"),
            Pair<Any, String>(R.drawable.doanthu_icon, "Doanh thu")
        )

        items.forEach { (icon, label) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                when (icon) {
                    is ImageVector -> Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )

                    is Int -> Icon(
                        painter = painterResource(id = icon),
                        contentDescription = label,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }

                Text(label, fontSize = 12.sp, color = Color.Black)
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeScreen()
}
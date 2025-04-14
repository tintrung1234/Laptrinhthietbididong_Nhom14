package com.example.ownerscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.format.TextStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(navController) }
                composable("revenue") { RevenueScreen(navController) }
            }
        }


    }
}


@Composable
fun HomeScreen(navController: NavHostController) {
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
            BottomNavigationBar(navController)
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
fun BottomNavigationBar(navController: NavHostController) {
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
                    .clickable {
                        if (label == "Doanh thu") {
                            navController.navigate("revenue")
                        }
                    }
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


// man hinh doanh thu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Doanh thu",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_2),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(70.dp)
                    )
                }
            )
        }
    ) { innerPadding ->

        // Bọc nội dung trong Column + VerticalScroll để tránh tràn
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {


            // Nút chọn ngày
            Button(
                onClick = { /* TODO: mở dialog chọn ngày */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier.wrapContentWidth()

            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Hôm nay")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RevenueCard(
                    title = "TỔNG DOANH THU",
                    value = "50.000.000",
                    color = Color.Red,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                RevenueCard(
                    title = "Số lượng đơn hoàn thành",
                    value = "15",
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            RevenueCard(
                title = "Dịch vụ bán chạy nhất",
                value = "Massage",
                color = Color.Red,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Doanh thu hôm nay",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = R.drawable.chart_doanhthu),
                contentDescription = "Chart",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp), // tránh để auto quá cao
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun RevenueCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Color.DarkGray),
        colors = CardDefaults.cardColors(containerColor = Color.White) // màu be nhạt
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()
    RevenueScreen(navController)
}
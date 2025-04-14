package com.example.spa_app

import android.graphics.Color.rgb
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spa_app.ui.theme.Spa_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Spa_appTheme {
                Controller()
            }
        }
    }
}

@Composable
fun TrangChu(navController: NavController) {
    Box {
        //Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp)
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxHeight()) {
                // Banner Group
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    Image(
                        painterResource(R.drawable.banner_trangchu),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 21.dp)
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Top
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 30.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        //Search bar
                        Row(
                            modifier = Modifier
                                .size(300.dp, 35.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(25.dp))
                                .padding(horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var text by remember { mutableStateOf("") }
                            BasicTextField(
                                value = text,
                                onValueChange = { text = it },
                                modifier = Modifier
                                    .size(250.dp, 30.dp)
                                    .background(color = Color.White),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (text.isEmpty()) {
                                            Text(
                                                "Tìm kiếm",
                                                color = Color(rgb(204, 204, 204)),
                                                fontSize = 18.sp
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )

                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                modifier = Modifier
                                    .size(16.dp, 20.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {

                                Image(
                                    painterResource(R.drawable.ph_magnifying_glass_thin),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        //Button notification
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .size(35.dp)
                                .offset(x = 5.dp),
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Image(
                                painterResource(R.drawable.line_md_bell_loop),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .background(color = Color.White)
                                    .padding(start = 1.dp)
                            )

                        }
                    }

                    //Category
                    Row(
                        //Set center box
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        //Set shadow
                        Row(
                            modifier = Modifier.shadow(8.dp, shape = RoundedCornerShape(8.dp)),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Row(
                                modifier = Modifier
                                    .size(340.dp, 76.dp)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                listOf(
                                    R.drawable.solar_calendar_bold to "Lịch hẹn",
                                    R.drawable.line_md_chat_filled to "Liên hệ",
                                    R.drawable.star_filled to "Đánh giá",
                                    R.drawable.mdi_voucher to "Mã giảm giá",
                                ).forEach { (icon, text) ->
                                    //Categories
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(50.dp)
                                    ) {
                                        Button(
                                            onClick = {},
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(
                                                    rgb(
                                                        219,
                                                        195,
                                                        124
                                                    )
                                                ).copy(alpha = 0.75f)
                                            ),
                                            modifier = Modifier.size(36.dp),
                                            contentPadding = PaddingValues(0.dp),
                                            shape = RoundedCornerShape(14.dp),
                                        ) {
                                            Image(
                                                painterResource(icon),
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Text(text, fontSize = 8.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Second Category below banner
            Row(
                modifier = Modifier.fillMaxHeight(0.17f),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {

                //Categories
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(horizontal = 15.dp)
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(
                        R.drawable.map_spa to "Spa",
                        R.drawable.emojione_nail_polish to "Nail",
                        R.drawable.twemoji_man_getting_massage to "Massage",
                        R.drawable.solar_cosmetic_bold to "Mỹ phẩm",
                        R.drawable.twemoji_girl_light_skin_tone to "Chăm sóc da"
                    ).forEach { (icon, text) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(70.dp)
                        ) {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(rgb(219, 195, 124)).copy(alpha = 0.75f)
                                ),
                                modifier = Modifier.size(36.dp),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(18.dp),
                            ) {
                                Image(
                                    painterResource(icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(text, fontSize = 8.sp)
                        }
                    }
                }
            }

            //Tile khuyen mai
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    "KHUYẾN MÃI HOT",
                    fontSize = 26.sp,
                    color = Color(rgb(255, 75, 75))
                )
                Image(
                    painterResource(R.drawable.foundation_burst_sale),
                    contentDescription = null,
                    modifier = Modifier.size(43.dp)
                )
            }

            // Products
            Row(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                repeat(5) {
                    Column(
                        modifier = Modifier.width(116.dp)
                    ) {
                        Image(
                            painterResource(R.drawable.khuyen_mai2),
                            contentDescription = null,
                            modifier = Modifier.size(116.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "MASSAGE XUA TAN NHỨC MỎI ",
                            fontSize = 11.sp,
                            lineHeight = 12.sp
                        )
                        Text(
                            "70.000đ",
                            fontSize = 16.sp,
                            color = Color(rgb(255, 75, 75))
                        )
                        Text(
                            "360.000đ",
                            fontSize = 13.sp,
                            color = Color(rgb(189, 189, 189)),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }

                    Spacer(Modifier.width(10.dp))
                }
            }

            Text(
                "Đội ngũ nhân viên chuyên nghiệp",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            //Doctor
            Row(
                modifier = Modifier.padding(20.dp)
            ) {
                Image(
                    painterResource(R.drawable.doi_ngu_bs1),
                    contentDescription = null,
                    modifier = Modifier.size(133.dp)
                )

                Column {
                    Text("Bác sĩ chuyên khoa da liễu 10 năm kinh nghiệm")
                    Row {
                        repeat(5) {
                            Image(
                                painterResource(R.drawable.group2),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(17.dp)
                                    .padding(1.dp)
                            )
                        }
                    }
                }
            }
        }

        //Menu
        MenuBar(navController)
    }
}

@Composable
fun Controller() {
    val navConTroller = rememberNavController()
    NavHost(navController = navConTroller, startDestination = "Thêm dịch vụ") {
        composable("Trang Chủ") { TrangChu(navConTroller) }
        composable("Đặt lịch") { DatLich(navConTroller) }
        composable("Đánh giá") { ReviewPage(navConTroller) }
        composable("Lịch sử") { LichSu(navConTroller) }
        composable("Mã giảm giá") { TrangMaGiamGia(navConTroller) }
        composable("Liên hệ") { TrangLienHe(navConTroller) }
        composable("Tài khoản") { TaiKhoan(navConTroller) }
        composable("Thêm dịch vụ") { ThemDichVu(navConTroller) }
        composable("Chỉnh sửa dịch vụ") { SuaDichVu(navConTroller) }
        composable("Trang đặt lịch") { Trangdatlich(navConTroller) }
    }
}

@Composable
fun MenuBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth()
                .background(color = Color(rgb(219, 195, 124)))
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf(
                R.drawable.icon_trang_chu to "Trang Chủ",
                R.drawable.icon_dat_lich to "Đặt lịch",
                R.drawable.icon_lich_su to "Lịch sử",
                R.drawable.icon_tai_khoan to "Tài khoản",
            ).forEach { (icon, route) ->
                val isSelected = currentRoute == route
                val interactionSource = remember { MutableInteractionSource() }
                val buttonPressColor by animateColorAsState(
                    if (isSelected) Color(rgb(140, 121, 63)) else Color.Transparent,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )

                Column(
                    modifier = Modifier.width(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(buttonPressColor, shape = RoundedCornerShape(30.dp))
                            .clickable(
                                onClick = {
                                    if (currentRoute != route) {
                                        navController.navigate(route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                        }
                                    }
                                },
                                interactionSource = interactionSource,
                                indication = null
                            )
                            .padding(25.dp, 9.dp)
                    ) {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp, 20.dp)
                        )
                    }
                    Text(route, fontSize = 12.sp, lineHeight = 14.sp)
                }
            }
        }
    }
}

@Composable
fun DatLich(navController: NavController) {
    Text("Trang dat lich")
    MenuBar(navController)
}

@Composable
fun LichSu(navController: NavController) {
    Text("Trang lich su")
    MenuBar(navController)
}

@Composable
fun TaiKhoan(navController: NavController) {
    Text("Trang Tai Khoan")
    MenuBar(navController)
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    Spa_appTheme {
        Controller()
    }
}
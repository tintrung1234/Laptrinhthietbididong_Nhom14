package com.example.spa_ower_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TrangMaGiamGia(navController: NavController) {
    Column(
        modifier = Modifier.background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            //Top nav
            TopLayout("Mã giảm giá", { navController.navigate("TrangChu") })

            Spacer(Modifier.height(10.dp))

            Text(
                "Số lượng có hạn dành cho những bạn nhanh nhất",
                fontSize = 13.sp, color = Color(0xFFADADAD),
                lineHeight = 14.sp
            )

            listOf(
                "10" to "3",
                "7" to "6",
                "5" to "10",
                "15" to "1"
            ).forEach { (number, quantity) ->


                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.height(112.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box() {
                        Box(
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .border(
                                    1.dp,
                                    color = Color.Black.copy(0.5f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 5.dp, end = 10.dp)
                            ) {
                                Text(
                                    "x$quantity",
                                    modifier = Modifier.align(Alignment.End)
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.42f)
                                        .align(Alignment.End)
                                        .offset(y = -15.dp)
                                ) {
                                    Image(
                                        painterResource(R.drawable.logo7),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(59.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "CODE: ",
                                            color = Color.Black.copy(0.23f),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 13.sp
                                        )
                                        Text(
                                            "SALE$number",
                                            color = Color(0xFFDBC37C),
                                            fontSize = 13.sp,
                                            lineHeight = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                        Image(
                            painterResource(R.drawable.image21),
                            contentDescription = null,
                            modifier = Modifier
                                .size(199.dp, 117.dp)
                                .offset(x = -5.dp),
                            contentScale = ContentScale.Fit
                        )
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.size(187.dp, 119.dp)

                        ) {
                            Text(
                                "$number%\n OFF",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
            }
        }
    }
}

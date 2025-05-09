package com.example.spa_app

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun DetailServiceScreen(
    navController: NavController,
    serviceID: String?,
    serviceViewModel: ServiceViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val serviceDetail = serviceViewModel.services.find { it.id == serviceID }
    if (serviceDetail != null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp, horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopLayout("Dịch vụ", { navController.popBackStack() })
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = serviceDetail.image,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(17.dp))
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 13.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Text(
                            text = serviceDetail.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = serviceDetail.rating.toString(),
                                    color = Color(0xFF818181)
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "",
                                    tint = Color(0xFFFFDC5D),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.image10),
                                    contentDescription = "",
                                    tint = Color(0xFF818181),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = serviceDetail.visitors.toString() + " lượt khách",
                                    color = Color(0xFF818181)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatCost(serviceDetail.price),
                                style = TextStyle(
                                    textDecoration = TextDecoration.LineThrough,
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            )
                            val discountPercent = serviceDetail.discount.toFloat() / 100f
                            val discountAmount = serviceDetail.price * discountPercent
                            val finalPrice = serviceDetail.price - discountAmount
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = formatCost(finalPrice),
                                fontSize = 24.sp,
                                color = Color.Red
                            )
                        }
                        if (categoryViewModel.categoriesID.indexOf(serviceDetail.categoryId) != -1) {
                            Text(
                                text = "Dịch vụ: ${
                                    categoryViewModel.categoriesName[categoryViewModel.categoriesID.indexOf(
                                        serviceDetail.categoryId
                                    )]
                                }",
                                color = Color(0xFFDBC37C),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0x40CCCCCC)
                        )
                    ) {
                        Text(
                            text = serviceDetail.description,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("serviceSelected", serviceDetail)
                        navController.navigate("TrangDatLich")
                              },
                    shape = RoundedCornerShape(7.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(270.dp)
                        .padding(bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFCFCA81)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 12.dp,       // khi bình thường
                    ),
                ) {
                    Text(
                        text = "Đặt lịch ngay",
                        fontSize = 17.sp
                    )
                }
            }

        }
    }
}
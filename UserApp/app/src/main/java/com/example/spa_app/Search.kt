package com.example.spa_app

import android.graphics.Color.rgb
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.spa_app.ui.theme.Spa_appTheme
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.runtime.LaunchedEffect


@Composable
fun Search(navController: NavController, serviceViewModel: ServiceViewModel = viewModel()) {
    val services = serviceViewModel.services
    var text by remember { mutableStateOf("") }

    //Show keyboard
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

// Request focus and show keyboard when screen is opened
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp)
                .padding(paddingValues) // Thêm padding từ Scaffold
        ) {
            // Header
            TopLayout("Tìm kiếm", { navController.popBackStack() })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                //Search bar
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(25.dp))
                        .padding(horizontal = 15.dp)
                        .border(1.dp, color = Color.Gray,shape = RoundedCornerShape(25.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(250.dp, 30.dp)
                            .background(color = Color.White)
                            .focusRequester(focusRequester),
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
                            .padding(end = 10.dp)
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
            }

            Spacer(Modifier.height(18.dp))

            fun removeAccents(input: String): String {
                val normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            }

            val filteredServices = if (text.isBlank()) {
                services
            } else {
                val searchQuery = removeAccents(text).lowercase()
                services.filter {
                    removeAccents(it.name).lowercase().contains(searchQuery)
                }
            }

            LazyColumn(
                modifier = Modifier.padding(20.dp)
                    .fillMaxHeight()
            ) {
                items(filteredServices) { service ->
                    val discountPercent = service.discount.toFloat() / 100f
                    val discountAmount = service.price * discountPercent
                    val finalPrice = service.price - discountAmount

                    val serviceId = service.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable(onClick = { navController.navigate("ChiTietDichVu/$serviceId") })
                    ) {
                        AsyncImage(
                            model = service.image,
                            contentScale = ContentScale.Crop,
                            contentDescription = service.name,
                            modifier = Modifier
                                .size(133.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = service.name,
                                fontSize = 20.sp,
                                lineHeight = 12.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(service.rating) {
                                    Image(
                                        painter = painterResource(R.drawable.group2),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(17.dp)
                                            .padding(1.dp)
                                    )
                                }
                            }
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
                                    text = "Lượt khách: " + service.visitors.toString(),
                                    color = Color(0xFF818181)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatCost(service.price),
                                    style = TextStyle(
                                        textDecoration = TextDecoration.LineThrough,
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(7.dp))
                                Text(
                                    text = formatCost(finalPrice),
                                    fontSize = 18.sp,
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview() {
//    Spa_appTheme {
//        val navController = rememberNavController()
//        Search(navController)
//    }
//}
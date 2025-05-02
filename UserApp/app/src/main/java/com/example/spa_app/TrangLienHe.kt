package com.example.spa_app

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun TrangLienHe(navController: NavController) {
    val contactViewModel: ContactViewModel = viewModel()
    var name = remember { mutableStateOf("") }
    var phone = remember { mutableStateOf("") }
    var address = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var message = remember { mutableStateOf("") }
    var successMessage = remember { mutableStateOf("") }
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
            TopLayout("Liên hệ", { navController.navigate("TrangChu") })

            Box(
                modifier = Modifier
                    .border(1.dp, color = Color.Black.copy(0.5f), shape = RoundedCornerShape(8.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Title
                    Row(
                        modifier = Modifier
                            .border(
                                1.dp,
                                color = Color.Black.copy(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Text(
                            "Chúng tôi luôn chân trọng từng ý kiến của quý khách hàng",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Mọi ý kiến đóng góp, phản hồi về dịch vụ quý khách hàng vui lòng điền vào biểu mẫu sau",
                        fontSize = 13.sp, color = Color(0xFFADADAD),
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp
                    )

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            val fields = listOf(
                                Triple(R.drawable.tdesign_user_filled, "Họ và tên", name),
                                Triple(R.drawable.image13, "Số điện thoại", phone),
                                Triple(R.drawable.location, "Địa chỉ", address),
                                Triple(R.drawable.image14, "Email", email)
                            )
                            fields.forEach { (icon, title, text) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painterResource(icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Text(title, modifier = Modifier.padding(horizontal = 7.dp))
                                    Icon(
                                        painterResource(R.drawable.important),
                                        contentDescription = null,
                                        modifier = Modifier.size(9.dp),
                                        tint = Color(0xFFDF2D2D)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(3.dp)
                                        .background(
                                            Color(0xFFF5F5F5),
                                            shape = RoundedCornerShape(60.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    BasicTextField(
                                        value = text.value,
                                        onValueChange = { text.value = it },
                                        textStyle = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        ),
                                        decorationBox = { innerTextField ->
                                            if (text.value.isEmpty()) {
                                                Text("Nhập nội dung...", color = Color.Gray)
                                            }
                                            innerTextField()
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painterResource(R.drawable.vector_pen),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                )
                                Text("Phản hồi", modifier = Modifier.padding(horizontal = 7.dp))
                                Icon(
                                    painterResource(R.drawable.important),
                                    contentDescription = null,
                                    modifier = Modifier.size(9.dp),
                                    tint = Color(0xFFDF2D2D)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(3.dp)
                                    .border(
                                        1.dp,
                                        color = Color.Black.copy(0.5f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 10.dp)

                            ) {
                                BasicTextField(
                                    value = message.value,
                                    onValueChange = { message.value = it },
                                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                    decorationBox = { innerTextField ->
                                        if (message.value.isEmpty()) {
                                            Text(
                                                "Chia sẽ một số phản hồi của bạn...",
                                                color = Color.Gray
                                            )
                                        }
                                        innerTextField()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row {
                Image(
                    painterResource(R.drawable.image20),
                    contentDescription = null,
                    modifier = Modifier.size(94.dp)
                )
                Column(
                    modifier = Modifier.height(94.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Hotline: 18001003", color = Color(0xFFDF2D2D))
                    Text(
                        "Cơ sở số 70 Tô Ký TCH 18 , Tân Chánh Hiệp , Quận 12, TPHCM",
                        fontSize = 13.sp, color = Color(0xFFADADAD),
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val contact = Contact(name.value, email.value, message.value)
                        contactViewModel.submitFeedback(
                            contact,
                            onSuccess = { successMessage.value = "Cảm ơn bạn đã liên hệ!" },
                            onError = { e -> successMessage.value = "Gửi thất bại: ${e.message}" }
                        )
                        navController.navigate("TrangChu")
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(200.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBC37C))
                ) {
                    Text("Gửi phản hồi")
                }
            }
        }
    }
}

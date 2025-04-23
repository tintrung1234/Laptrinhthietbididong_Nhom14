package com.example.spa_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun InforScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Thông tin cá nhân", { navController.navigate("TrangChu") })
//        inforLayout("ABC")
        val viewModel: AuthViewModel = viewModel()
        inforLayout(navController, viewModel)
    }
}

private fun onclick() {}

@Composable
fun inforLayout(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    Column {
        Spacer(modifier = Modifier.weight(0.5f))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo), // Ảnh từ drawable
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp) // Đặt kích thước ảnh
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .offset(x = 55.dp, y = 45.dp) // Di chuyển toàn bộ IconButton
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera), // Ảnh từ drawable
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(27.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                        .background(Color.White)
                )
            }

        }
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Xin chào ABC!",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        var editInfor by remember { mutableStateOf(false) }
        //=====================1 Hàm riêng========================
        inforItem("name", editInfor)
        inforItem("phone", editInfor)
        inforItem("email", editInfor)
        inforItem("pass", editInfor)
        //========================================================
        Spacer(modifier = Modifier.weight(1f))
        Row {

            Button(
                onClick = { editInfor = !editInfor },
                modifier = Modifier
//                .align(Alignment.CenterHorizontally)
                ,
                shape = RoundedCornerShape(12.dp), // Bo góc
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#D6D183"))
                ) // Đổi màu nền
            ) {
                Row {
                    Text(
                        text = if (editInfor) "Lưu" else "Chỉnh sửa thông tin",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Icon(
                        imageVector = if (editInfor) Icons.Default.Done else Icons.Default.Create,
                        contentDescription = "icon",
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Button(
                onClick = {
                    viewModel.signOut()
                    navController.navigate("TrangChu")
                },
                modifier = Modifier
//                .align(Alignment.CenterHorizontally)
                ,
                shape = RoundedCornerShape(12.dp), // Bo góc
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#D6D183"))
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.signout_ic),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "icon",
                        tint = Color.White
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inforItem(title: String, editInfor: Boolean) {
    Row(
        modifier = Modifier.padding(top = 20.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(

            imageVector = when (title) {
                "name" -> Icons.Default.AccountCircle
                "phone" -> Icons.Default.Phone
                "email" -> Icons.Default.Email
                "pass" -> Icons.Default.Lock
                else -> Icons.Default.AccountCircle
            },
            contentDescription = "icon",
        )
        Text(
            text = when (title) {
                "name" -> "Họ và tên"
                "phone" -> "Số điện thoại"
                "email" -> "Email"
                "pass" -> "Mật khẩu"
                else -> ""
            },
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
    var passwordVisible by remember { mutableStateOf(false) }
    var inforName by remember {
        mutableStateOf(
            when (title) {
                "name" -> "Nguyễn Văn A"
                "phone" -> "099123456"
                "email" -> "abc@gmail.com"
                "pass" -> "password"
                else -> ""
            }
        )
    }
    TextField(
        value = inforName,
        onValueChange = { inforName = it },
        enabled = editInfor,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(android.graphics.Color.parseColor("#E0E0E0")), // Màu nền khi bị disabled
            disabledIndicatorColor = Color.Transparent // Ẩn viền khi bị disabled

        ),
        textStyle = TextStyle(fontSize = 16.sp),
        shape = RoundedCornerShape(12.dp), // Bo góc
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        visualTransformation = if (title == "pass" && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (title == "pass") {
            {
                val icon = Icons.Default.Lock
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                }
            }
        } else null
    )
}



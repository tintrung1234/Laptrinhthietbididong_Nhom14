package com.example.spa_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotifyScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    notifyViewModel: NotifyViewModel = viewModel()
) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val notifications = notifyViewModel.notifications

    if (userId == null) {
        // Điều hướng đến màn hình đăng nhập hoặc thông báo cho người dùng
        navController.navigate("TaiKhoan")
        return // Dừng thực thi màn hình này nếu không có userId
    }
    LaunchedEffect(Unit) {
        notifyViewModel.addMissingTypeToNotifications()
        notifyViewModel.loadUserNotifications(userId)
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Thông báo") {
            navController.navigate("TrangChu")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notifications) { item ->
                NotiItem(
                    title = item.contentForUser,
                    timestamp = item.timestamp,
                    navController,
                    item
                )
            }
        }
    }
}

@Composable
fun NotiItem(title: String, timestamp: Long , navController: NavController,item: UserNotification) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(android.graphics.Color.parseColor("#E0E0E0")))
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.height(60.dp)) {
                Text(text = title, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(
                        text = convertTimestamp(timestamp),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = Color(0x80000000),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { when(item.type.lowercase()){
                            "datlich" -> navController.navigate("lichsu")
                            "suathongtin"-> navController.navigate("TaiKhoan")
                            else ->{}
                        } },
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .height(20.dp)
                    ) {
                        Text(
                            text = "Xem chi tiết >>",
                            textDecoration = TextDecoration.Underline,
                            color = Color(0x80000000),
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

fun convertTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}


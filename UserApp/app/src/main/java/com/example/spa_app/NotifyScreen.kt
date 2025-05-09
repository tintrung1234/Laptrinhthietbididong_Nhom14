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
        notifyViewModel.loadUserNotifications(userId)

        notifications.forEach {
            notifyViewModel.markNotificationAsSeen(it.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Thông báo") {
            navController.navigate("TrangChu")
        }
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Không có thông báo")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notifications) { item ->
                    NotiItem(
                        title = item.contentForUser,
                        timestamp = item.timestamp,
                        navController,
                        onClick = {
                            notifyViewModel.markNotificationAsSeen(item.id)
                            if (item.contentForUser == "Bạn đã thay đổi thông tin cá nhân") {
                                navController.navigate("TaiKhoan")
                            } else {
                                navController.navigate("LichSu")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotiItem(title: String, timestamp: Long, navController: NavController, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(android.graphics.Color.parseColor("#E0E0E0")))
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(text = title, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = convertTimestamp(timestamp),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = Color(0x80000000),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onClick,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .height(20.dp)
                    ) {
                        Text(
                            text = "Xem chi tiết >>",
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.align(Alignment.CenterVertically),
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
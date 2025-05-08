package com.example.spa_ower_app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NotifyScreen(navController: NavController, notifyViewModel: NotifyViewModel = viewModel()) {


    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""
    val notifications = notifyViewModel.notifications
    if (userId == null) {
        // Điều hướng đến màn hình đăng nhập hoặc thông báo cho người dùng
        navController.navigate("TaiKhoan")
        return // Dừng thực thi màn hình này nếu không có userId
    }
    // Tải thông báo mỗi khi màn hình NotifyScreenOwner được gọi
    LaunchedEffect(Unit) {
        notifyViewModel.loadNotifications(userId )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Thông báo", { navController.navigate("TrangChu") })

        // Hiển thị thông báo cho Owner
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notifications) { item ->
                NotiItem(
                    title = item.contentForOwner,
                    timestamp = item.timestamp,
                    navController
                )
            }
        }
    }
}


@Composable
fun NotiItem(title: String, timestamp: Long, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))  // Bo góc
            .background(Color(android.graphics.Color.parseColor("#E0E0E0")))// Màu nền
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.height(60.dp)) {
                Text(text = title, fontSize = 18.sp)  // Hiển thị nội dung thông báo
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(
                        text = convertTimestamp(timestamp),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = Color(0x80000000),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { navController.navigate("lichsu") },
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

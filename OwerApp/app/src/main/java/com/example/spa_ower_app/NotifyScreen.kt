package com.example.spa_ower_app

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

@Composable
fun NotifyScreen(navController: NavController, notifyViewModel: NotifyViewModel = viewModel()) {
    val context = LocalContext.current
    val notifications = notifyViewModel.notifications

    // Lắng nghe sự kiện thông báo
    LaunchedEffect(Unit) {
        notifyViewModel.notificationEvent.collect { notification ->
            showPopupNotification(context, notification.contentForOwner)
        }
    }

    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""
    if (userId == null) {
        navController.navigate("TaiKhoan")
        return
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

@SuppressLint("MissingPermission")
fun showPopupNotification(context: Context, message: String) {
    val channelId = "owner_channel"

    // Tạo notification channel nếu cần
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Thông báo từ khách hàng",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    // Tạo Intent để mở MainActivity và điều hướng đến LichSu
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("navigate_to", "lichsu")
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_logo)
        .setContentTitle("Spa Owner")
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(Random.nextInt(), notification)
}


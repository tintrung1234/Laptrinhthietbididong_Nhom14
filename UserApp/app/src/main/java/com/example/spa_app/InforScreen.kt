package com.example.spa_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

@Composable
fun InforScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Thông tin cá nhân", { navController.navigate("TrangChu") })
        val viewModel: AuthViewModel = viewModel()
        InforLayout(navController, viewModel)
    }
}

@Composable
fun InforLayout(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val currentUser = auth.currentUser
    val userDocRef = currentUser?.let { firestore.collection("Users").document(it.uid) }
    var notifyViewModel: NotifyViewModel = viewModel()
    // State cho thông tinS
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var editMode by remember { mutableStateOf(false) }

    // Lấy dữ liệu Firestore khi mở
    LaunchedEffect(Unit) {
        if (userDocRef != null) {
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        name = document.getString("name") ?: currentUser.displayName.orEmpty()
                        phone = document.getString("phone") ?: currentUser.phoneNumber.orEmpty()
                        email = document.getString("email") ?: currentUser.email.orEmpty()
                    } else {
                        // Chưa có dữ liệu Firestore
                        name = currentUser.displayName.orEmpty()
                        phone = currentUser.phoneNumber.orEmpty()
                        email = currentUser.email.orEmpty()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("InforLayout", "Error getting user info", e)
                }
        }
    }

    val photoUrl = currentUser?.photoUrl

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(0.2f))

        // Ảnh đại diện
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = photoUrl ?: R.drawable.user_image,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            )
//            IconButton(
//                onClick = {},
//                modifier = Modifier
//                    .offset(x = 55.dp, y = 45.dp)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_camera),
//                    contentDescription = "icon",
//                    modifier = Modifier
//                        .size(27.dp)
//                        .clip(CircleShape)
//                        .border(1.dp, Color.Black, CircleShape)
//                        .background(Color.White)
//                )
//            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Tên người dùng
        Text(
            text = name.ifEmpty { "Xin chào" },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Các thông tin
        InforItem("name", name, { name = it }, editMode)
        InforItem("phone", phone, { phone = it }, editMode)
        InforItem("email", email, { email = it }, editMode)

        Spacer(modifier = Modifier.weight(1f))

        // Các nút chức năng
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (editMode) {
                        // Nếu đang ở chế độ "Lưu", thì lưu dữ liệu Firestore
                        val updatedData = mapOf(
                            "name" to name,
                            "phone" to phone,
                            "email" to email
                        )
                        if (userDocRef != null) {
                            userDocRef.set(updatedData)
                                .addOnSuccessListener {
                                    Log.d("InforLayout", "User info updated")
                                    currentUser?.uid?.let { uid ->
                                        notifyViewModel.createUpdateInforNotification(userId = uid)
                                    }
                                    showPopupInforNotification(
                                        context = context,
                                        message = "Thông tin cá nhân đã được cập nhật thành công!"
                                    )
                                }

                                .addOnFailureListener { e ->
                                    Log.e("InforLayout", "Failed to update info", e)
                                }
                        }
                    }
                    editMode = !editMode
                },
                modifier = Modifier
//                .align(Alignment.CenterHorizontally)
                ,
                shape = RoundedCornerShape(12.dp), // Bo góc
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#D6D183"))
                ) // Đổi màu nền,
                ,
                contentPadding = PaddingValues(horizontal = 7.dp)
            ) {
                Row {
                    Text(
                        text = if (editMode) "Lưu" else "Chỉnh sửa thông tin",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Icon(
                        imageVector = if (editMode) Icons.Default.Done else Icons.Default.Create,
                        contentDescription = null
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Button(
                onClick = {
                    viewModel.signOut()
                    navController.navigate("TrangChu")
                },
                shape = RoundedCornerShape(12.dp), // Bo góc
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#D6D183"))
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.signout_ic),
                    contentDescription = "Sign out",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InforItem(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    editMode: Boolean
) {
    Row(
        modifier = Modifier.padding(top = 20.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (title) {
                "name" -> Icons.Default.AccountCircle
                "phone" -> Icons.Default.Phone
                "email" -> Icons.Default.Email
                else -> Icons.Default.AccountCircle
            },
            contentDescription = null
        )
        Text(
            text = when (title) {
                "name" -> "Họ và tên"
                "phone" -> "Số điện thoại"
                "email" -> "Email"
                else -> ""
            },
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
    }

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        enabled = editMode,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(android.graphics.Color.parseColor("#E0E0E0")),
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(fontSize = 14.sp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}

fun showPopupInforNotification(context: Context, message: String) {
    val channelId = "booking_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Thông báo đặt lịch", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    // Tạo Intent để mở MainActivity và điều hướng đến LichSu
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("navigate_to", "taikhoan") // bạn xử lý intent này trong MainActivity
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_logo)
        .setContentTitle("Spa App")
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(Random.nextInt(), notification)
}


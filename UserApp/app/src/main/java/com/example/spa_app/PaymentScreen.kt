package com.example.spa_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.random.Random

@Composable
fun PaymentScreen(
    navController: NavController,
    appointmentId: String?,
    appointmentViewModel: AppointmentViewModel = viewModel(),
    serviceViewModel: ServiceViewModel = viewModel(),
    discountViewModel: DiscountViewModel = viewModel()
) {
    var notifyViewModel: NotifyViewModel = viewModel()
    val context = LocalContext.current

    // Lắng nghe sự kiện thông báo
    LaunchedEffect(key1 = true) {
        notifyViewModel.notificationEvents.collect { notification ->
            showPopupNotification(context, notification.contentForUser)
        }
    }

    // Get the Appointment matching the ID
    val appointmentIndex = appointmentId

    val vouchers = discountViewModel.vouchers
    var voucher by remember { mutableStateOf("") }
    var acceptVoucher = remember { mutableStateOf(false) }

    // Check when voucher input changes
    LaunchedEffect(voucher) {
        acceptVoucher.value = vouchers.any { it.code.equals(voucher, ignoreCase = true) }
    }

    var voucherValue = 0

    var finalPrice = 0f

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // State for user info
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    if (appointmentIndex != null) {
        val appointment =
            appointmentViewModel.appointments.find { it.id == appointmentId }
        Log.d("appointment", "$appointment")

        // Get service
        val serviceIndex = appointment?.servicesId.toString()
        if (serviceIndex != null) {

            val service = serviceViewModel.services.find { it.id == serviceIndex }

            var totalPrice: Float = 0f

            if (service != null) {
                val discountPercent = service.discount.toFloat() / 100f
                val discountAmount = service.price * discountPercent
                totalPrice = service.price - discountAmount

                if (appointment != null) {
                    name = appointment.userName
                    phone = appointment.phone
                    email = appointment.email
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 30.dp, horizontal = 20.dp)
                ) {
                    item { TopLayout("Thanh toán", onclick = { navController.popBackStack() }) }
                    item {
                        AddressCustomer(
                            name,
                            phone,
                            email
                        )
                    }
                    item { ServiceRow(service.image, service.name, service.price, totalPrice) }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Mã voucher",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        MyCard {
                            BasicTextField(
                                value = voucher,
                                onValueChange = { voucher = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp),
                                decorationBox = { innerTextField ->
                                    Box(
                                    ) {
                                        if (voucher.isEmpty()) {
                                            Text(
                                                "Nhập mã khuyến mãi",
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    }
                    item { PaymentMethod() }


                    item {
                        MyCard {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Chi tiết thanh toán",
                                    fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                var voucherAmount = 0f
                                finalPrice = totalPrice - voucherAmount

                                if (acceptVoucher.value) {
                                    val voucher = vouchers.find { it.code == voucher }
                                    if (voucher != null) {
                                        voucherValue = voucher.value
                                    }
                                    val discountPercent = voucherValue / 100f
                                    voucherAmount = totalPrice * discountPercent
                                    finalPrice = totalPrice - voucherAmount
                                }


                                itemPaymentDetail("Tổng tiền:", totalPrice)
                                itemPaymentDetail("Giảm giá:", voucherAmount)
                                itemPaymentDetail("Thành tiền:", finalPrice)

                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    if (appointment != null) {
                                        appointmentViewModel.updateAppointmentInFirestore(
                                            appointment,
                                            finalPrice
                                        )
                                        if (acceptVoucher.value) {
                                            val voucher = vouchers.find { it.code == voucher }
                                            if (voucher != null) {
                                                voucherValue = voucher.value
                                            }
                                            if (voucher != null) {
                                                discountViewModel.updateVoucherInFirestore(voucher)
                                            }
                                        }

                                        currentUser?.uid?.let { uid ->
                                            notifyViewModel.createNotification(uid)
                                        }
                                    }
                                    navController.navigate("LichSu")
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFDBC37C)
                                ),
                                modifier = Modifier.align(Alignment.BottomEnd)
                            ) {
                                Text(
                                    text = "Xác nhận thanh toán",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(vertical = 10.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun onclick() {}

@Composable
fun MyCard(content: @Composable () -> Unit) {
    Spacer(modifier = Modifier.height(13.dp))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD9D9D9)
        )
    ) {
        Card(
            modifier = Modifier.padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                content()
            }
        }
    }
    Spacer(modifier = Modifier.height(13.dp))
}

@Composable
fun AddressCustomer(name: String, phone: String, email: String) {
    MyCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(4f)
            ) {
                Row {
                    Text(name)
                    Text(
                        text = " (+84)" + "${phone}",
                        color = Color(0x80000000)
                    )
                }
                Text(
                    text = email,
                    fontSize = 12.sp
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .weight(0.4f)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "",
                    modifier = Modifier
                        .size(29.dp)
                )
            }
        }
    }
}

@Composable
fun ServiceRow(image: String, title: String, price: Float, discount: Float) {
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Gói dịch vụ",
        color = Color(0xFFDBC37C),
        fontSize = 25.sp
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            itemService(
                image,
                title,
                discount,
                price
            )
        }
    }
}

//@Preview (showBackground = true)
@Composable
fun itemService(img: String, name: String, costSale: Float, cost: Float) {

    Column(
        modifier = Modifier
            .width(130.dp)
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = img,
            contentDescription = "",
            modifier = Modifier
                .height(140.dp)
                .width(140.dp)
        )
        Text(
            text = name,
            fontSize = 11.sp,
            lineHeight = 19.sp
        )
        Text(
            text = formatCost(costSale),
            color = Color.Red,
            fontSize = 13.sp
        )
        Text(
            text = formatCost(cost),
            style = TextStyle(
                textDecoration = TextDecoration.LineThrough,
                color = Color.Gray,
                fontSize = 12.sp
            )
        )
    }
    Spacer(modifier = Modifier.width(30.dp))
}

@Composable
fun PaymentMethod() {
    MyCard {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Phương thức thanh toán",
                fontSize = 20.sp,
                color = Color(0xFFDBC37C)
            )
            itemPaymentMethod(R.drawable.nimbus_cash, "Thanh toán trực tiếp tại Spa")
        }
    }
}

@Composable
fun itemPaymentMethod(icon: Any, name: String) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is ImageVector -> {
                // Nếu iconResource là ImageVector (ví dụ, từ Icons.Default)
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = Color(0xFFFFA629)
                )
            }

            is Int -> {
                // Nếu iconResource là một ID tài nguyên (dành cho ảnh bitmap)
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp) // Cỡ ảnh có thể thay đổi
                )
            }

            else -> {
                // Xử lý khi tham số không phải kiểu mong đợi
                Text("Invalid resource")
            }
        }
        Text(
            text = name
        )
        Spacer(modifier = Modifier.weight(1f))
        var checked by remember { mutableStateOf(false) }
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFDBC37C),          // Màu khi được chọn
                uncheckedColor = Color.LightGray,    // Màu khi không chọn
                checkmarkColor = Color.White,        // Màu dấu tích (✓)
            )
        )
    }
}

@Composable
fun itemPaymentDetail(title: String, cost: Float) {
    Row {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatCost(cost),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = if (title == "Giảm giá") Color(0xFFFF8736) else Color.Black
        )
    }
}

fun formatCost(cost: Float): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = '.'
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(cost.toInt()) + "đ"
}

fun showPopupNotification(context: Context, message: String) {
    val channelId = "booking_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Thông báo đặt lịch", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    // Tạo Intent để mở MainActivity và điều hướng đến LichSu
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("navigate_to", "lichsu") // bạn xử lý intent này trong MainActivity
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

package com.example.spa_app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppointmentDetailScreen(navController: NavController, appointmentID: Int?) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp)
                .padding(paddingValues) // Thêm padding từ Scaffold
        ) {
            // Header
            TopLayout("Thông tin lịch hẹn", {navController.popBackStack() })

            Spacer(modifier = Modifier.height(8.dp))

            // Banner
            Image(
                painter = painterResource(id = R.drawable.khuyen_mai2),
                contentDescription = "Service Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp)),

                )

            Spacer(modifier = Modifier.height(8.dp))

            // Service Info
            Text(
                text = "MASSAGE XUA TAN NHỨC MỎI",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Row {
                Text(
                    text = "400.000đ",
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "70.000đ",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(text = "Dịch vụ: Massage", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = "• Thư giãn toàn thân – Giảm căng thẳng, mệt mỏi.\n" +
                        "• Bấm huyệt – Cải thiện tuần hoàn, giảm đau nhức.\n" +
                        "• Phục hồi năng lượng – Tăng cường sức khỏe, ngủ ngon hơn.\n" +
                        "• Thời gian thực hiện: 60 phút",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Customer Info Box
            Card(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Chi tiết", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(4.dp))
                    DetailRow("Khách hàng:", "Nguyễn Văn A", Icons.Default.Person)
                    DetailRow("Số điện thoại:", "0909xxxxxx", Icons.Default.Phone)
                    DetailRow("Địa chỉ:", "123/321 ABC", Icons.Default.LocationOn)
                    DetailRow("Thời gian phục vụ:", "3/3/2025 - 14:15", R.drawable.donghocat)
                    DetailRow("Thời gian đặt đơn:", "1/3/2025", R.drawable.clock)
                    DetailRow("Kỹ thuật viên:", "Nguyễn Văn B", Icons.Default.Person)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payment Section
            Card(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Thanh toán", fontWeight = FontWeight.Bold, color = Color.Red)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng tiền:")
                        Text("158.000đ")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Giảm giá:")
                        Text("0đ")
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Thành tiền:", fontWeight = FontWeight.Bold)
                        Text("158.000đ", fontWeight = FontWeight.Bold)
                    }
                    Text("(Đã thanh toán)", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}




@Composable
fun DetailRow(label: String, value: String, icon: Any) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        when (icon) {
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            is Int -> Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.Unspecified // giữ màu gốc cho drawable
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "$label $value", fontSize = 14.sp)
    }
    Spacer(modifier = Modifier.height(4.dp))
}
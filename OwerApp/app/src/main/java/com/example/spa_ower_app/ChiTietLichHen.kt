package com.example.spa_ower_app

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AppointmentDetailScreen(
    navController: NavController,
    appointmentID: String?,
    appointmentViewModel: AppointmentViewModel = viewModel(),
    serviceViewModel: ServiceViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
    staffViewModel: StaffViewModel = viewModel()
) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp)
                .padding(paddingValues) // Add padding from Scaffold
        ) {
            // Header
            item {
                TopLayout("Thông tin lịch hẹn", { navController.popBackStack() })
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                // Banner
                if (appointmentID != null) {
                    val firestore = FirebaseFirestore.getInstance()
                    val auth = FirebaseAuth.getInstance()
                    val currentUser = auth.currentUser
                    val userDocRef =
                        currentUser?.let { firestore.collection("Users").document(it.uid) }

                    // State for user info
                    var name by remember { mutableStateOf("") }
                    var phone by remember { mutableStateOf("") }
                    var email by remember { mutableStateOf("") }

                    // Get the Appointment matching the ID
                    val appointmentIndex = appointmentID

                    if (appointmentIndex != null) {
                        val appointment =
                            appointmentViewModel.appointments.find { it.id == appointmentID }
                        Log.d("appointment", "$appointment")

                        // Get service and staff id
                        val serviceIndex = appointment?.servicesId.toString()
                        val staffIndex = appointment?.staffId.toString()

                        if (serviceIndex != null && staffIndex != null) {

                            val service = serviceViewModel.services.find { it.id == serviceIndex }
                            val staff = staffViewModel.staffs.find { it.id == staffIndex }

                            LaunchedEffect(Unit) {
                                if (userDocRef != null) {
                                    userDocRef.get()
                                        .addOnSuccessListener { document ->
                                            if (document != null && document.exists()) {
                                                name = document.getString("name")
                                                    ?: currentUser.displayName.orEmpty()
                                                phone = document.getString("phone")
                                                    ?: currentUser.phoneNumber.orEmpty()
                                                email = document.getString("email")
                                                    ?: currentUser.email.orEmpty()
                                            } else {
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

                            if (service != null && staff != null && appointment != null) {
                                AsyncImage(
                                    model = service.image,
                                    contentDescription = "Service Banner",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Service Info
                                Text(
                                    text = service.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Row {
                                    Text(
                                        text = formatCost(service.price),
                                        textDecoration = TextDecoration.LineThrough,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = formatCost((100 - service.discount) * service.price / 100),
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (categoryViewModel.categoriesID.indexOf(service.categoryId) != -1) {
                                    Text(
                                        text = "Dịch vụ: ${
                                            categoryViewModel.categoriesName[categoryViewModel.categoriesID.indexOf(
                                                service.categoryId
                                            )]
                                        }", fontSize = 14.sp, color = Color.Gray
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Description
                                Text(
                                    text = service.description,
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
                                        DetailRow("Khách hàng:", name, Icons.Default.Person)
                                        DetailRow("Số điện thoại:", phone, Icons.Default.Phone)
                                        // DetailRow("Địa chỉ:", "123/321 ABC", Icons.Default.LocationOn)
                                        if (appointment != null) {
                                            DetailRow(
                                                "Thời gian phục vụ:",
                                                appointment.pickedDate,
                                                R.drawable.donghocat
                                            )
                                        }
                                        DetailRow(
                                            "Thời gian đặt đơn:",
                                            appointment.orderDate,
                                            R.drawable.clock
                                        )
                                        DetailRow(
                                            "Kỹ thuật viên:",
                                            "${staff.name}",
                                            Icons.Default.Person
                                        )
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
                                        Text(
                                            "Thanh toán",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Red
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Tổng tiền:")
                                            Text(formatCost(service.price))
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Giảm giá:")
                                            Text(formatCost(service.discount * service.price / 100))
                                        }
                                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Thành tiền:", fontWeight = FontWeight.Bold)
                                            Text(
                                                "${formatCost(appointment.totalValues)}",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        CheckPayment(appointment.paymentMethod)
                                    }
                                }
                                Spacer(Modifier.height(10.dp))
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Button(
                                        onClick = {
                                            val updatedAppointment = appointment.copy(state = 1)
                                            appointmentViewModel.updateAppointmentInFirestore(
                                                updatedAppointment
                                            )
                                            navController.navigate("LichSu")
                                        },
                                        shape = RoundedCornerShape(7.dp),
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .width(270.dp)
                                            .padding(bottom = 10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFCFCA81)
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(
                                            defaultElevation = 12.dp,       // khi bình thường
                                        ),
                                    ) {
                                        Text(
                                            text = "Xác nhận",
                                            fontSize = 17.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text("Không tìm thấy lịch hẹn", color = Color.Red)
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
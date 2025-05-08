package com.example.spa_ower_app

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.TextStyle
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
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

//@Preview(showBackground = true)
@Composable
fun HistoryScreen(
    navController: NavController,
    appointmentViewModel: AppointmentViewModel = viewModel(),
) {
    //Get info user
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userDocRef = currentUser?.let { firestore.collection("Users").document(it.uid) }

    val appointment = appointmentViewModel.appointments.filter { it.state == 0 }
    val servicesViewModel: ServiceViewModel = viewModel()

    // State cho thông tinS
    var admin by remember { mutableStateOf(0) }

    // Fetch admin field on Composable entry
    LaunchedEffect(Unit) {
        userDocRef?.get()
            ?.addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    admin = document.getLong("admin")?.toInt() ?: 0
                }
            }
            ?.addOnFailureListener { e ->
                Log.e("InforLayout", "Error getting user info", e)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Lịch sử", { navController.navigate("TrangChu") })
        if (userDocRef != null && admin == 1) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                ),
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(appointment) { appointment ->
                        itemCardHistory(
                            appointment,
                            appointment.id,
                            navController,
                            servicesViewModel
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Bạn chưa đăng nhập",
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun itemCardHistory(
    appointment: Appointment,
    appointmentId: String,
    navController: NavController,
    serviceViewModel: ServiceViewModel = viewModel()
) {
    val appointmentViewModel: AppointmentViewModel = viewModel()
    var appointments = appointmentViewModel.appointments

    val service = serviceViewModel.services.find { it.id == appointment.servicesId }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = appointment.orderDate,
                    color = Color(0x801E1E1E),
                )
                CheckStateOrder(appointment.state)
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (service != null && appointments.indexOf(appointment) != -1) {
                    itemServiceHistory(service, appointmentId, navController)
                }
            }
            Text(
                text = "Tổng tiền: ${formatCost(appointment.totalValues)}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End // Căn văn bản bên phải
            )
            CheckPayment(appointment.paymentMethod)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun itemServiceHistory(service: Service, appointmentID: String, navController: NavController) {
    Log.d("appoinmentID", "$appointmentID")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
            .clickable(onClick = {
                navController.navigate("ChiTietLichHen/$appointmentID")
            }),
    ) {
        AsyncImage(
            model = service.image,
            contentDescription = "",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = service.name,
                fontSize = 13.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = formatCost(service.price),
                    style = TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = formatCost((100 - service.discount) * service.price / 100),
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }
        }
    }
}

fun formatCost(cost: Float): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = '.'
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(cost.toInt()) + "đ"
}

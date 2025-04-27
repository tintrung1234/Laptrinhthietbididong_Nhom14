package com.example.spa_app

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

//@Preview(showBackground = true)
@Composable
fun HistoryScreen(
    navController: NavController,
    appointmentViewModel: AppointmentViewModel,
){
    val currentUser = FirebaseAuth.getInstance().currentUser
    val appointments = appointmentViewModel.appointments.filter { it.UserId == currentUser?.uid }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ){
        TopLayout("Lịch sử", {navController.popBackStack() })
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
                items(appointments){appointment ->
                    itemCardHistory(appointment, navController)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun itemCardHistory(appointment: Appointment, navController: NavController){
    val serviceViewModel: ServiceViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()
    var appointments = appointmentViewModel.appointments
    var appointmentsID = appointmentViewModel.appointmentsID
    var servicesID = serviceViewModel.servicesID
    val index = servicesID.indexOf(appointment.ServicesId)
    val service = serviceViewModel.services.getOrNull(index)
    if (service != null) {
        itemServiceHistory(service, appointmentsID[appointments.indexOf(appointment)], navController)
    }


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
                    text = appointment.OrderDate,
                    color = Color(0x801E1E1E),
                )
                CheckStateOrder(appointment.State)
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (service != null) {
                    itemServiceHistory(service, appointmentsID[appointments.indexOf(appointment)],navController)
                }
            }
            Text(
                text = "Tổng tiền: ${appointment.TotalValues}}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End // Căn văn bản bên phải
            )
            CheckPayment(appointment.PaymentMethod)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun itemServiceHistory(service: Service, appointmentID: Int, navController: NavController){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
            .clickable(onClick = {
                navController.navigate("ChiTietLichHen/$appointmentID")
            }),
    ) {
        AsyncImage(
            model = service.Image,
            contentDescription ="",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = service.Name,
                fontSize = 13.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = formatCost(service.Price),
                    style = TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = formatCost((100-service.Discount)*service.Price/100),
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }
        }
    }
}

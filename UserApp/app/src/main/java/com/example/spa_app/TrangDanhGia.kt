package com.example.spa_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewPage(navController: NavController){
    Column(
        modifier = Modifier.background(color = Color.White)
    ) {
        val appointmentViewModel: AppointmentViewModel = viewModel()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val appointment = appointmentViewModel.appointments.filter { it.userId == currentUser?.uid && it.paymentMethod == true }
        val servicesViewModel: ServiceViewModel = viewModel()
        val staffViewModel: StaffViewModel = viewModel()
        val items = mutableMapOf<Appointment, Service>()
        if (appointment.isNotEmpty()) {
            for (item in appointment) {
                servicesViewModel.services.find { it.id == item.servicesId }
                    ?.let { items[item]=it}
            }
        }
        var selectedOption by remember { mutableStateOf("Chọn") }
        var appointmentID by remember { mutableStateOf("")}
        var selectedIndexService by remember { mutableStateOf(-1) }
        var selectedIndexStaff by remember { mutableStateOf(-1) }
        var selectedService by remember { mutableStateOf<Service?>(null) }
        var selectedStaff by remember { mutableStateOf<Staff?>(null) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            //Top nav
            TopLayout("Đánh giá", { navController.navigate("TrangChu") })

            Spacer(Modifier.height(20.dp))

            //Dich vu
            Box(
                modifier = Modifier
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))

                    //Title
                    Row {
                        Text(
                            "Dịch vụ bạn muốn đánh giá",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )
                    }
                    Text("Lưu ý: chỉ đánh giá được dịch vụ đã trải nghiệm",
                        fontSize = 11.sp, color = Color(0xFFADADAD),
                        textAlign = TextAlign.Center)

                    Spacer(Modifier.height(12.dp))

                    //Drop box

                    var expanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopStart)
                            .fillMaxWidth()
                            .border(
                                2.dp,
                                color = Color(0xFF544C4C24).copy(0.14f),
                                shape = RoundedCornerShape(8.dp)
                            )
//                            .padding(horizontal = 16.dp, vertical = 7.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextField(
                                    value = selectedOption,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 12.sp
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.White,
                                        focusedContainerColor = Color.White,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    ),
                                )
                            }

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                items.forEach { (appointment, service) ->
                                    DropdownMenuItem(
                                        text = { Text(service.name + " " + appointment.pickedDate) },
                                        onClick = {
                                            selectedOption = service.name + " " + appointment.pickedDate
                                            appointmentID = appointment.id
                                            selectedService = service
                                            selectedStaff = staffViewModel.staffs.find { it.id == appointment.staffId }
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))


                }
            }

            Spacer(Modifier.height(20.dp))
            if (appointmentID != null){
                Text("Đánh giá dịch vụ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listOf(
                        R.drawable.star to "Rất tệ",
                        R.drawable.star to "Tệ",
                        R.drawable.star to "Bình thường",
                        R.drawable.star to "Tốt",
                        R.drawable.star to "Tuyệt vời"
                    ).forEachIndexed{index, (icon, text) ->
                        Column(
                            modifier = Modifier
                                .width(70.dp)
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painterResource(icon),
                                contentDescription = "star",
                                modifier = Modifier.size(37.dp)
                                    .clickable { selectedIndexService = index },
                                tint = if (index <= selectedIndexService) Color(0xFFFFC107) else Color(0xFF888888)
                            )
                            Text(text, fontSize = 10.sp,
                                textAlign = TextAlign.Center)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text("Đánh giá nhân viên",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(20.dp))

                if (selectedStaff != null){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = selectedStaff!!.image,
                        contentDescription = null,
                        modifier = Modifier.size(90.dp))
                    Spacer(Modifier.width(20.dp))
                    Text(
                        selectedStaff!!.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                }
                }
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listOf(
                        R.drawable.star to "Rất tệ",
                        R.drawable.star to "Tệ",
                        R.drawable.star to "Bình thường",
                        R.drawable.star to "Tốt",
                        R.drawable.star to "Tuyệt vời"
                    ).forEachIndexed{index, (icon, text) ->
                        Column(
                            modifier = Modifier
                                .width(70.dp)
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painterResource(icon),
                                contentDescription = "star",
                                modifier = Modifier.size(37.dp)
                                    .clickable { selectedIndexStaff = index },
                                tint = if (index <= selectedIndexStaff) Color(0xFFFFC107) else Color(0xFF888888)
                            )
                            Text(text, fontSize = 10.sp,
                                textAlign = TextAlign.Center)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (selectedService != null && selectedStaff != null){
                                servicesViewModel.updateRateFirestore(selectedService!!, selectedIndexService)
                                staffViewModel.updateRateFirestore(selectedStaff!!, selectedIndexStaff)
                                navController.navigate("TrangChu")
                            }
                                  },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBC37C))
                    ) {
                        Text("Gửi đánh giá")
                    }
                }
            }
        }
    }
}

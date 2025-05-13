package com.example.spa_app

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrangDatLich(navController: NavController, serviceSelected: Service? = null) {
    val authViewModel: AuthViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val serviceViewModel: ServiceViewModel = viewModel()
    val staffViewModel: StaffViewModel = viewModel()
    val db = FirebaseFirestore.getInstance()

    // Trạng thái thông tin người dùng

    var selectedService by remember { mutableStateOf(serviceSelected) } // Khai báo selectedService
    var selectedStaff by remember { mutableStateOf<Staff?>(null) }

    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userDocRef = currentUser?.let { firestore.collection("Users").document(it.uid) }

    var hoTen by remember { mutableStateOf("") }
    var soDienThoai by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    // Lấy dữ liệu Firestore khi mở
    LaunchedEffect(Unit) {
        if (userDocRef != null) {
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        Log.d("UserDoc", "$document")
                        hoTen = document.getString("name") ?: currentUser.displayName.orEmpty()
                        soDienThoai =
                            document.getString("phone") ?: currentUser.phoneNumber.orEmpty()
                        email = document.getString("email") ?: currentUser.email.orEmpty()

                        Log.d("Họ và tên", "$hoTen")
                        Log.d("Số điện thoại", "$soDienThoai)")
                        Log.d("Email", "$email")
                    } else {
                        // Chưa có dữ liệu Firestore
                        hoTen = currentUser.displayName.orEmpty()
                        soDienThoai = currentUser.phoneNumber.orEmpty()
                        email = currentUser.email.orEmpty()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("InforLayout", "Error getting user info", e)
                }
        }
    }

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Thanh điều hướng trên cùng
        TopLayout("Đặt lịch", { navController.navigate("TrangChu") })

        // Thông tin người dùng
        Box(
            modifier = Modifier
                .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Thông Tin Đặt Lịch",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    //username
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.tdesign_user_filled),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("Họ và tên", modifier = Modifier.padding(horizontal = 7.dp))
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                            .background(
                                Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(60.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        BasicTextField(
                            value = hoTen,
                            onValueChange = {
                                hoTen = it
                            },
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            decorationBox = { innerTextField ->
                                if (hoTen.isEmpty()) {
                                    Text("Nhập nội dung...", color = Color.Gray)
                                }
                                innerTextField()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    //phone
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.vector5),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("Số điện thoại", modifier = Modifier.padding(horizontal = 7.dp))
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                            .background(
                                Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(60.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        BasicTextField(
                            value = soDienThoai,
                            onValueChange = {
                                soDienThoai = it
                            },
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            decorationBox = { innerTextField ->
                                if (soDienThoai.isEmpty()) {
                                    Text("Nhập nội dung...", color = Color.Gray)
                                }
                                innerTextField()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    //email
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.image14),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("Email", modifier = Modifier.padding(horizontal = 7.dp))
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                            .background(
                                Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(60.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        BasicTextField(
                            value = email,
                            onValueChange = {
                                email = it
                            },
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            decorationBox = { innerTextField ->
                                if (email.isEmpty()) {
                                    Text("Nhập nội dung...", color = Color.Gray)
                                }
                                innerTextField()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Chọn dịch vụ
        Box(
            modifier = Modifier
                .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Text(
                        "Dịch vụ bạn muốn làm",
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

                Spacer(Modifier.height(12.dp))

                var expanded by remember { mutableStateOf(false) }


                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp,
                            color = Color(0xFF544C4C24).copy(0.14f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    TextField(
                        value = selectedService?.name ?: "Chọn dịch vụ",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        serviceViewModel.services.forEach { service ->
                            DropdownMenuItem(
                                text = { Text(service.name) },
                                onClick = {
                                    selectedService = service
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Chọn khung giờ

        val days = remember { generateDaysForMonth() }
        val times = listOf(
            listOf("09:00", "09:45", "10:30", "11:15"),
            listOf("12:00", "12:45", "13:30", "14:15"),
            listOf("15:00", "15:45", "16:30", "17:15")
        )
        val statusMap = mapOf(
            "09:00" to "available",
            "09:45" to "available",
            "10:30" to "available",
            "11:15" to "available",
            "12:00" to "available",
            "12:45" to "available",
            "13:30" to "available",
            "14:15" to "available",
            "15:00" to "available",
            "15:45" to "available",
            "16:30" to "available",
            "17:15" to "available"
        )

        TimeSlotPicker(
            days = days,
            times = times,
            statusMap = statusMap,
            selectedDate = selectedDate,
            selectedTime = selectedTime,
            onDateSelected = { date -> selectedDate = date },
            onTimeSelected = { time -> selectedTime = time ?: "" }
        )

        Spacer(Modifier.height(10.dp))
        // Chọn kỹ thuật viên
        Row {
            Text(
                "Kĩ thuật viên phù hợp",
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
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
        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

            val isValidDateTime = selectedDate.isNotBlank() && selectedTime.isNotBlank()
            val dateTimeString = if (isValidDateTime) "${selectedDate} $selectedTime" else null

            staffViewModel.staffs.forEachIndexed { index, staff ->
                var checked by remember { mutableStateOf(false) }

                val isBusy = if (dateTimeString != null) {
                    try {
                        val selectedStart = LocalDateTime.parse(dateTimeString, formatter)
                        appointmentViewModel.appointments.any { appt ->
                            appt.staffId == staff.id &&
                                    selectedService != null && run {
                                val apptStart = LocalDateTime.parse(appt.pickedDate, formatter)
                                val apptDuration =
                                    serviceViewModel.services.find { it.id == appt.servicesId }?.overalTime ?: 0

                                isOverlapping(
                                    selectedStart,
                                    selectedService!!.overalTime,
                                    apptStart,
                                    apptDuration
                                )

                            }
                        }

                    } catch (e: DateTimeParseException) {
                        false
                    }
                } else {
                    false
                }
                if (!isBusy) {
                    Column(
                        modifier = Modifier
                            .width(130.dp)
                            .padding(horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = staff.image,
                            contentDescription = null,
                            modifier = Modifier.size(90.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        checked = !checked
                                        if (checked) {
                                            selectedStaff = staff// Save the staff ID when checked

                                        } else {
                                            selectedStaff =
                                                null // Reset the selected staff ID when unchecked
                                        }
                                    }
                            ) {
                                drawCircle(
                                    color = if (checked) Color(0xFFDBC37C) else Color(0xFFD9D9D9)
                                )
                                if (checked) {
                                    drawLine(
                                        color = Color.White,
                                        strokeWidth = 4f,
                                        start = Offset(size.width * 0.3f, size.height * 0.5f),
                                        end = Offset(size.width * 0.45f, size.height * 0.7f)
                                    )
                                    drawLine(
                                        color = Color.White,
                                        strokeWidth = 4f,
                                        start = Offset(size.width * 0.45f, size.height * 0.7f),
                                        end = Offset(size.width * 0.7f, size.height * 0.3f)
                                    )
                                }
                            }
                        }
                        Text(staff.name, fontSize = 12.sp)
                    }

                }
            }

        }

        Spacer(Modifier.height(20.dp))
        var error by remember { mutableStateOf<String?>(null) }
        if (error != null) {
            Text(
                text = error!!,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
        // Nút gửi đặt lịch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    //Kiem tra day du thong tin
                    if (hoTen.isBlank() || soDienThoai.isBlank() || email.isBlank() ||
                        selectedService == null || selectedTime.isBlank() || selectedStaff == null
                    ) {
                        val missingFields = mutableListOf<String>()

                        if (hoTen.isBlank()) missingFields.add("Họ tên")
                        if (soDienThoai.isBlank()) missingFields.add("Số điện thoại")
                        if (email.isBlank()) missingFields.add("Email")
                        if (selectedService == null) missingFields.add("Dịch vụ")
                        if (selectedTime.isBlank()) missingFields.add("Thời gian")
                        if (selectedStaff == null) missingFields.add("Nhân viên")

                        Log.d("Error", "Thiếu thông tin: ${missingFields.joinToString(", ")}")
                        error = "Vui lòng chọn đầy đủ thông tin"
                        return@Button
                    }

                    val discountPercent = selectedService!!.discount / 100f
                    val voucherAmount = selectedService!!.price * discountPercent
                    val finalPrice = selectedService!!.price - voucherAmount

                    val staffIndex = selectedStaff!!.id
                    val serviceIndex = selectedService!!.id
                    val userId = authViewModel.authState?.uid ?: "anonymous"
                    val appointment = Appointment(
                        userId = userId,
                        userName = hoTen,
                        phone = soDienThoai,
                        email = email,
                        staffId = staffIndex,
                        servicesId = serviceIndex,
                        orderDate = SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(Date()),
                        pickedDate = "$selectedDate $selectedTime",
                        state = 0,
                        totalValues = finalPrice,
                        paymentMethod = false
                    )

                    val appointmentsRef = db.collection("Appointments")

                    serviceViewModel.updateVistor(selectedService!!)

                    appointmentsRef.add(appointment)
                        .addOnSuccessListener { documentReference ->
                            val appointmentId = documentReference.id

                            // Optionally update Firestore document with this ID inside the object
                            appointmentsRef.document(appointmentId)
                                .update(
                                    "id",
                                    appointmentId
                                ) // This assumes your Appointment class has an "id" field


                            Log.d("appointmentIDdatlich", "$appointmentId")
                            navController.navigate("TrangThanhToan/$appointmentId")
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(150.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBC37C))
            ) {
                Text("Đặt lịch")
            }
        }
    }
}

fun generateDaysForMonth(): List<String> {
    val today = LocalDate.now()
    val endDate = today.plusMonths(1)
    val formatter = DateTimeFormatter.ofPattern("dd/MM")
    val days = mutableListOf<String>()

    var date = today
    while (date.isBefore(endDate)) {
        if (!date.isBefore(today)) { // Ensure date is not in the past
            val dayOfWeek = when (date.dayOfWeek) {
                DayOfWeek.MONDAY -> "Thứ 2"
                DayOfWeek.TUESDAY -> "Thứ 3"
                DayOfWeek.WEDNESDAY -> "Thứ 4"
                DayOfWeek.THURSDAY -> "Thứ 5"
                DayOfWeek.FRIDAY -> "Thứ 6"
                DayOfWeek.SATURDAY -> "Thứ 7"
                DayOfWeek.SUNDAY -> "Chủ Nhật"
            }
            days.add("$dayOfWeek\n${date.format(formatter)}/${date.year}")
        }
        date = date.plusDays(1)
    }

    return days
}

fun isOverlapping(
    selectedStart: LocalDateTime,
    selectedDuration: Int,
    apptStart: LocalDateTime,
    apptDuration: Int
): Boolean {
    val selectedEnd = selectedStart.plusMinutes(selectedDuration.toLong())
    val apptEnd = apptStart.plusMinutes(apptDuration.toLong())
    return selectedStart < apptEnd && selectedEnd > apptStart
}


@Composable
fun TimeSlotPicker(
    days: List<String>, // e.g., "Thứ 2\n06/05/2025"
    times: List<List<String>>,
    statusMap: Map<String, String>,
    selectedDate: String,
    selectedTime: String?,
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String?) -> Unit
) {
    var currentDateIndex by remember { mutableStateOf(days.indexOfFirst { it.contains(selectedDate) }) }
    val listState = rememberLazyListState()

    // Scroll to selected index when it changes
    LaunchedEffect(currentDateIndex) {
        if (currentDateIndex in days.indices) {
            listState.animateScrollToItem(currentDateIndex)
        }
    }

    fun onArrowClick(isNext: Boolean) {
        currentDateIndex = when {
            isNext -> (currentDateIndex + 1) % days.size
            else -> (currentDateIndex - 1 + days.size) % days.size
        }
        // Extract full date part after '\n'
        val fullDate = days[currentDateIndex].substringAfter("\n")
        onDateSelected(fullDate)
    }

    Column(
        modifier = Modifier
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text("Chọn Ngày Giờ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painterResource(R.drawable.important),
                    contentDescription = null,
                    modifier = Modifier.size(9.dp),
                    tint = Color(0xFFDF2D2D)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                ColorLegend(color = Color(0xFF9EFFC6), label = "Chưa Chọn")
                Spacer(Modifier.width(10.dp))
                ColorLegend(color = Color(0xFFFFFF66), label = "Đang chọn")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(70.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color(0xFFBDBDBD).copy(0.6f)),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Trước",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp)
                        .clickable { onArrowClick(false) }
                )
            }

            LazyRow(
                state = listState,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                itemsIndexed(days) { index, day ->
                    val dayDate = day.substringAfter("\n")
                    val isSelected = selectedDate == dayDate

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(
                                if (isSelected) Color(0xFFFCEA2B) else Color(0xFFBDBDBD).copy(0.6f)
                            )
                            .clickable {
                                currentDateIndex = index
                                onDateSelected(dayDate)
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            day,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color(0xFFBDBDBD).copy(0.6f)),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Sau",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp)
                        .clickable { onArrowClick(true) }
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        times.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { time ->
                    val status = statusMap[time]
                    val isSelected = time == selectedTime
                    val backgroundColor = when {
                        isSelected -> Color(0xFFFCEA2B)
                        status == "available" -> Color(0xFF07FD72).copy(0.5f)
                        status == "full" -> Color(0xFFBDBDBD)
                        else -> Color.White
                    }
                    val colorFontStyle = when {
                        isSelected -> Color.White
                        status == "available" -> Color.White
                        else -> Color.White
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                            .background(backgroundColor)
                            .clickable(enabled = status == "available") {
                                onTimeSelected(if (isSelected) null else time)
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(time, fontWeight = FontWeight.SemiBold, color = colorFontStyle)
                    }
                }
            }
        }
    }
}


@Composable
fun ColorLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .height(12.dp)
                .width(33.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp)
    }
}

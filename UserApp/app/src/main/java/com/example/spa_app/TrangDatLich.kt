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
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrangDatLich(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val serviceViewModel: ServiceViewModel = viewModel()
    val staffViewModel: StaffViewModel = viewModel()
    val db = FirebaseFirestore.getInstance()

    // Trạng thái thông tin người dùng
    var hoTen by remember { mutableStateOf("") }
    var soDienThoai by remember { mutableStateOf("") }
    var diaChi by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf<Service?>(null) } // Khai báo selectedService
    var selectedStaff by remember { mutableStateOf<Staff?>(null) }

    // Lấy dữ liệu người dùng đã đăng nhập
    LaunchedEffect(authViewModel.authState) {
        authViewModel.authState?.let { user ->
            authViewModel.fetchUserData(user.uid) { fetchedUser ->
                fetchedUser?.let {
                    hoTen = it.name
                    soDienThoai = it.phone
                    diaChi = it.email // Giả sử địa chỉ được lưu dưới dạng email, điều chỉnh nếu cần
                }
                if(hoTen == null || soDienThoai == null || diaChi == null){
                    Log.d("thong tin", "$hoTen")
                    Log.d("thong tin", "$soDienThoai")
                    Log.d("thong tin", "$diaChi")
                }
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
                    listOf(
                        Triple(R.drawable.tdesign_user_filled, "Họ và tên", hoTen),
                        Triple(R.drawable.vector5, "Số điện thoại", soDienThoai),
                        Triple(R.drawable.location, "Địa chỉ", diaChi)
                    ).forEach { (icon, title, value) ->
                        val text = remember { mutableStateOf(value) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painterResource(icon),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(title, modifier = Modifier.padding(horizontal = 7.dp))
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
                                value = text.value,
                                onValueChange = {
                                    text.value = it
                                    when (title) {
                                        "Họ và tên" -> hoTen = it
                                        "Số điện thoại" -> soDienThoai = it
                                        "Địa chỉ" -> diaChi = it
                                    }
                                },
                                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                decorationBox = { innerTextField ->
                                    if (text.value.isEmpty()) {
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
            staffViewModel.staffs.forEachIndexed { index, staff ->
                var checked by remember { mutableStateOf(false) }

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
                                .clickable { checked = !checked
                                    if (checked) {
                                        selectedStaff = staff// Save the staff ID when checked

                                    } else {
                                        selectedStaff = null // Reset the selected staff ID when unchecked
                                    }}
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

        Spacer(Modifier.height(20.dp))

        // Chọn khung giờ
        var selectedDate by remember { mutableStateOf("09/03") }
        var selectedTime by remember { mutableStateOf("") }

        val days = listOf("Thứ 7\n08/03", "Chủ Nhật\n09/03", "Thứ 2\n10/03", "Thứ 3\n11/03")
        val times = listOf(
            listOf("09:00", "09:45", "10:30", "11:15"),
            listOf("12:00", "12:45", "13:30", "14:15"),
            listOf("15:00", "15:45", "16:30", "17:15")
        )
        val statusMap = mapOf(
            "09:00" to "available", "09:45" to "full", "10:30" to "available", "11:15" to "available",
            "12:00" to "full", "12:45" to "available", "13:30" to "available", "14:15" to "full",
            "15:00" to "available", "15:45" to "full", "16:30" to "full", "17:15" to "available"
        )

        TimeSlotPicker(
            days = days,
            times = times,
            statusMap = statusMap,
            selectedDate = selectedDate,
            selectedTime = selectedTime,
            onDateSelected = { date -> selectedDate = date }, // Sửa it thành tên rõ ràng
            onTimeSelected = { time -> selectedTime = time ?: "" } // Sửa it thành tên rõ ràng
        )

        Spacer(Modifier.height(10.dp))

        // Nút gửi đặt lịch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (hoTen.isBlank() || soDienThoai.isBlank() || diaChi.isBlank() ||
                        selectedService == null || selectedTime.isBlank() || selectedStaff == null
                    ) {
                        val missingFields = mutableListOf<String>()

                        if (hoTen.isBlank()) missingFields.add("Họ tên")
                        if (soDienThoai.isBlank()) missingFields.add("Số điện thoại")
                        if (diaChi.isBlank()) missingFields.add("Địa chỉ")
                        if (selectedService == null) missingFields.add("Dịch vụ")
                        if (selectedTime.isBlank()) missingFields.add("Thời gian")
                        if (selectedStaff == null) missingFields.add("Nhân viên")

                        Log.d("Error", "Thiếu thông tin: ${missingFields.joinToString(", ")}")
                        return@Button
                    }

                    val staffIndex = selectedStaff!!.id
                    val serviceIndex = selectedService!!.id
                    val userId = authViewModel.authState?.uid ?: "anonymous"
                    val appointment = Appointment(
                        userId = userId,
                        staffId = staffIndex,
                        servicesId = serviceIndex,
                        orderDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                        pickedDate = "$selectedDate $selectedTime",
                        state = 0,
                        totalValues = selectedService!!.price,
                        paymentMethod = false
                    )

                    db.collection("Appointments")
                        .add(appointment)
                        .addOnSuccessListener {

                            navController.navigate("TrangThanhToan")
                        }
                        .addOnFailureListener { e ->
                            // Xử lý lỗi (ví dụ: hiển thị toast hoặc snackbar)
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

@Composable
fun TimeSlotPicker(
    days: List<String>,
    times: List<List<String>>,
    statusMap: Map<String, String>,
    selectedDate: String,
    selectedTime: String?,
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String?) -> Unit
) {
    var currentDateIndex by remember { mutableStateOf(days.indexOf(selectedDate)) }

    fun onArrowClick(isNext: Boolean) {
        currentDateIndex = when {
            isNext -> (currentDateIndex + 1) % days.size
            else -> (currentDateIndex - 1 + days.size) % days.size
        }
        onDateSelected(days[currentDateIndex].substringAfter("\n"))
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
                ColorLegend(color = Color(0xFF9EFFC6), label = "Còn chỗ")
                ColorLegend(color = Color.LightGray, label = "Hết chỗ")
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
                    .background(color = Color(0xFFBDBDBD).copy(0.6f)),
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                items(days) { day ->
                    val isSelected = day.contains(selectedDate)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(
                                if (isSelected) Color(0xFFFCEA2B) else Color(0xFFBDBDBD).copy(0.6f)
                            )
                            .clickable { onDateSelected(day.substringAfter("\n")) }
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
                    .background(color = Color(0xFFBDBDBD).copy(0.6f)),
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
                        status == "full" -> Color.Black
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

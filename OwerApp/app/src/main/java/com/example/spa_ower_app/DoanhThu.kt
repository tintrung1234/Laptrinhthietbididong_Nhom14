package com.example.spa_ower_app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueScreen(navController: NavHostController) {
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val serviceViewModel: ServiceViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()

    // Bọc nội dung trong Column + VerticalScroll để tránh tràn
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopLayout("Doanh Thu", { navController.popBackStack() })

        val context = LocalContext.current
        val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

        // Store the selected date as a string
        var selectedDate by remember { mutableStateOf(dateFormatter.format(Date())) }

        // DatePickerDialog setup
        val calendar = Calendar.getInstance()
        val datePickerDialog = remember {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = dateFormatter.format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        // Button to open the DatePickerDialog
        Button(
            onClick = { datePickerDialog.show() },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.wrapContentWidth()
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text(selectedDate) // Show current selected date
        }

        Spacer(modifier = Modifier.height(16.dp))
        val appointments =
            appointmentViewModel.appointments.filter { it.state == 1 && it.orderDate == selectedDate }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val tongDoanhThu = appointments.sumOf { it.totalValues.toDouble() }.toFloat()

            RevenueCard(
                title = "TỔNG DOANH THU",
                value = formatCost(tongDoanhThu),
                color = Color.Red,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            val soDonHoanThanh = appointments.sumOf { it.state }
            RevenueCard(
                title = "Số lượng đơn hoàn thành",
                value = soDonHoanThanh.toString(),
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val mostPopularCategory = getMostBookedCategoryName(
            appointments,
            serviceViewModel.services,
            categoryViewModel.categoriesName
        )

        RevenueCard(
            title = "Dịch vụ bán chạy nhất",
            value = mostPopularCategory,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Doanh thu hôm nay",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(8.dp))

        //chart
        val services = serviceViewModel.services
        val categoryNames = categoryViewModel.categoriesName

        val categoryTotals = remember(appointments, services, categoryNames) {
            computeTotalsByCategoryName(appointments, services, categoryNames)
        }

        CategoryTotalChart(categoryTotals)
    }
}


@Composable
fun RevenueCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Color.DarkGray),
        colors = CardDefaults.cardColors(containerColor = Color.White) // màu be nhạt
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CategoryTotalChart(categoryTotals: Map<String, Float>) {
    val maxTotal = categoryTotals.values.maxOrNull() ?: 1f

    val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFFFFC107),
        Color(0xFF03A9F4),
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFFFF5722),
        Color(0xFF795548),
    )

    val sortedEntries = categoryTotals.entries.toList()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray.copy(0.1f), RoundedCornerShape(5.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        sortedEntries.forEachIndexed { index, (categoryName, total) ->
            val barHeight = (total / maxTotal) * 150f
            val color = colors[index % colors.size]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                var totalPrice = formatCost(total)
                Text(
                    totalPrice,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
//                    modifier = Modifier.width(60.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(barHeight.dp)
                        .background(color, shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = categoryName,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
//                    modifier = Modifier.width(60.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


fun computeTotalsByCategoryName(
    appointments: List<Appointment>,
    services: List<Service>,
    categoryNames: List<String>
): Map<String, Float> {
    val serviceMap = services.associateBy { it.id }

    return appointments.groupBy { appointment ->
        val categoryId = serviceMap[appointment.servicesId]?.categoryId
        if (categoryId != null && categoryId in categoryNames.indices)
            categoryNames[categoryId]
        else
            "Loading"
    }.mapValues { entry ->
        entry.value.sumOf { it.totalValues.toDouble() }.toFloat()
    }
}

fun getMostBookedCategoryName(
    appointments: List<Appointment>,
    services: List<Service>,
    categoryNames: List<String>
): String {
    // Map services by ID for fast lookup
    val serviceMap = services.associateBy { it.id }

    // Count how many appointments fall into each categoryId
    val categoryCount = appointments
        .mapNotNull { serviceMap[it.servicesId]?.categoryId }
        .groupingBy { it }
        .eachCount()

    // Find the categoryId with the most appointments
    val mostBookedCategoryId = categoryCount.maxByOrNull { it.value }?.key

    return if (mostBookedCategoryId != null && mostBookedCategoryId in categoryNames.indices) {
        categoryNames[mostBookedCategoryId]
    } else {
        "Loading"
    }
}









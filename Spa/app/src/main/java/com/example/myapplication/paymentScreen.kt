package com.example.myapplication

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
import androidx.compose.material.icons.filled.Payments
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
@Composable
fun paymentScreen(){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ){
        item { topLayout("Thanh toán",{ onclick() }) }
        item { AddressCustomer("Chị Nguyễn A ", "(+84)980026728", "334/123/42 Phạm Văn Đồng phường 15 quận Bình Thạnh, TP Hồ Chí Minh") }
        item { ServiceRow() }
        item { VoucherInput() }
        item { PaymentMethod() }
        item { PaymentDetail() }
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Button(
                    onClick = {},
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

private fun onclick(){}

@Composable
fun MyCard(content: @Composable () -> Unit){
    Spacer(modifier = Modifier.height(13.dp))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD9D9D9)
        )
    ){
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
fun AddressCustomer(name: String, phone: String, address: String){
    MyCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.weight(4f)
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "",
                        tint = Color(0xFFF24822)
                    )
                    Text(name)
                    Text(
                        text = " ${phone}",
                        color = Color(0x80000000)
                    )
                }
                Text(
                    text = address,
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
fun ServiceRow(){
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Gói dịch vụ",
        color = Color(0xFFDBC37C),
        fontSize = 25.sp
    )
    LazyRow (
        modifier = Modifier.fillMaxWidth()
    ) {
        item { itemService(R.drawable.dichvu1, "COMBO 4 CHĂM SÓC DA LẤY NHÂN MỤN 88K", 88000f, 400000f) }
        item { itemService(R.drawable.dichvu2, "MASSAGE XUA TAN NHỨC MỎI", 70000f, 360000f) }
    }
}

//@Preview (showBackground = true)
@Composable
fun itemService(img: Int, name: String, costSale: Float, cost: Float){
    Column (
        modifier = Modifier.width(130.dp)
            .wrapContentHeight()
    ){
        Image(
            painter = painterResource(id = img),
            contentDescription ="",
            modifier = Modifier.height(140.dp).width(140.dp)
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
fun VoucherInput(){
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Mã voucher",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
    MyCard {
        var text by remember { mutableStateOf("") }

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            decorationBox = { innerTextField ->
                Box(
                ) {
                    if (text.isEmpty()) {
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

@Composable
fun PaymentMethod(){
    MyCard {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Phương thức thanh toán",
                fontSize = 20.sp,
                color = Color(0xFFDBC37C)
            )
            itemPaymentMethod(Icons.Default.Payments, "Thanh toán trực tiếp tại Spa")
            itemPaymentMethod(R.drawable.logo_momo,"Thanh toán qua momo")
        }
    }
}

@Composable
fun itemPaymentMethod(icon: Any, name: String){

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
fun PaymentDetail(){
    MyCard {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Chi tiết thanh toán",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
            itemPaymentDetail("Tổng tiền:", 158000f)
            itemPaymentDetail("Giảm giá:", 0f)
            itemPaymentDetail("Thành tiền:", 158000f)

        }
    }
}

@Composable
fun itemPaymentDetail(title: String, cost: Float){
    Row {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatCost(cost),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = if(title == "Giảm giá") Color(0xFFFF8736) else Color.Black
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
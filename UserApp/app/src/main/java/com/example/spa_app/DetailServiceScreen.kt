package com.example.spa_app

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DetailServiceScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Dịch vụ", {navController.navigate("TrangChu")})
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            LazyColumn (
                modifier = Modifier.fillMaxSize()
            ){
                item { imageSection() }
                item { inforService() }
                item { descService() }
            }
            Button(
                onClick = {navController.navigate("TrangDatLich")},
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
                    text = "Đặt lịch ngay",
                    fontSize = 17.sp
                )
            }
        }
    }
}

@Composable
fun imageSection(){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Image(
            painter = painterResource(id = R.drawable.khuyen_mai1),
            contentDescription = "",
            contentScale = ContentScale.Crop, // hoặc Fit nếu muốn ảnh không bị cắt
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(17.dp))
        )
        Spacer(modifier = Modifier.height(7.dp))
        LazyRow {
            item { Image(
                painter = painterResource(id = R.drawable.khuyen_mai2),
                contentDescription = "",
                modifier = Modifier.padding(end = 5.dp)
            ) }
            item { Image(
                painter = painterResource(id = R.drawable.khuyen_mai2),
                contentDescription = "",
                modifier = Modifier.padding(end = 5.dp)
            ) }
            item { Image(
                painter = painterResource(id = R.drawable.khuyen_mai2),
                contentDescription = "",
                modifier = Modifier.padding(end = 5.dp)
            ) }
            item { Image(
                painter = painterResource(id = R.drawable.khuyen_mai2),
                contentDescription = "",
                modifier = Modifier.padding(end = 5.dp)
            ) }
        }
    }
}

@Composable
fun inforService(){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 13.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = "CHĂM SÓC DA 7 BƯỚC CHUYÊN SÂU",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Đánh giá: 4.8",
                    color = Color(0xFF818181)
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = Color(0xFFFFDC5D),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically){
                Icon(
                    painter = painterResource(R.drawable.image10),
                    contentDescription = "",
                    tint = Color(0xFF818181),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = " Đã bán: 12312",
                    color = Color(0xFF818181)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "213.123đ",
                style = TextStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = "123.221đ",
                fontSize = 24.sp,
                color = Color.Red
            )
        }
        Text(
            text = "Dịch vụ: Chăm sóc da",
            color = Color(0xFFDBC37C),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun descService(){
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x40CCCCCC)
        )
    ) {
        Text(
            text = "    Dịch vụ Chăm sóc da 7 bước chuyên sâu mang đến trải nghiệm thư giãn và phục hồi làn da một cách toàn diện. Với quy trình chuyên nghiệp, sử dụng sản phẩm cao cấp và kỹ thuật tiên tiến, làn da của bạn sẽ được làm sạch sâu, cấp ẩm tối ưu và tái tạo hiệu quả.\n" +
                    "\n" +
                    "    Quy trình 7 bước chuyên sâu:" +
                    "\n✅ Làm sạch da – Loại bỏ bụi bẩn, dầu thừa và lớp trang điểm." +
                    "\n✅ Tẩy tế bào chết – Giúp da sáng mịn, hấp thụ dưỡng chất tốt hơn." +
                    "\n✅ Xông hơi & hút dầu thừa – Thải độc, làm sạch sâu lỗ chân lông." +
                    "\n✅ Massage thư giãn – Tăng cường lưu thông máu, giảm căng thẳng." +
                    "\n✅ Đắp mặt nạ dưỡng chất – Phục hồi và cấp ẩm chuyên sâu." +
                    "\n✅ Dưỡng chất đặc trị – Tùy theo tình trạng da (mụn, thâm, lão hóa…)." +
                    "\n✅ Bảo vệ da – Thoa kem dưỡng và chống nắng giúp khóa ẩm, bảo vệ da tối đa." +
                    "\n" +
                    "\n" +
                    "\uD83D\uDC86 Hiệu quả sau liệu trình:" +
                    "\n✨ Làn da sạch sâu, căng bóng và rạng rỡ." +
                    "\n✨ Giảm dầu thừa, se khít lỗ chân lông." +
                    "\n✨ Cấp ẩm và làm dịu da tức thì." +
                    "\n✨ Hỗ trợ cải thiện các vấn đề về da như mụn, thâm, lão hóa." +
                    "\n" +
                    "\uD83D\uDCCD Dành cho ai?" +
                    "\n✔\uFE0F Người muốn duy trì làn da khỏe đẹp." +
                    "\n✔\uFE0F Da mụn, nhạy cảm hoặc cần phục hồi." +
                    "\n✔\uFE0F Da xỉn màu, thiếu sức sống cần cấp ẩm và tái tạo." +
                    "\n" +
                    "\uD83D\uDCC6 Đặt lịch ngay hôm nay để trải nghiệm sự khác biệt! \uD83D\uDC96",
            modifier = Modifier.padding(15.dp)
        )
    }
}
package com.example.spa_ower_app

import android.graphics.Color.rgb
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.components.Lazy

@Composable
fun ServiceScreen(
    navController: NavController,
    serviceViewModel: ServiceViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
){
    val services = serviceViewModel.services
    var categoriesID = categoryViewModel.categoriesID
    var categoriesName = categoryViewModel.categoriesName
    var categoryID by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp))
        { TopLayout("Dịch vụ",{navController.popBackStack()})}
        Card(
            modifier = Modifier.fillMaxSize()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    clip = false // để không cắt nội dung
                ),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Đặt elevation = 0 để tránh đổ bóng mặc định,
        ) {
            CustomTopAppBar(categoriesID, categoriesName, categoryID) {categoryID = it}
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(20.dp)
            ) {
                item { itemCardService(services.filter { it.categoryId==categoryID }, "Bán chạy nhất", R.drawable.ic_hot, navController) }
                item { itemCardService(services.filter { it.categoryId==categoryID }, "Ưu đãi", R.drawable.ic_sale, navController) }
                item { itemCardService(services.filter { it.categoryId==categoryID }, "Các gói khác",-1, navController) }

            }
        }
    }
}

@Composable
fun CustomTopAppBar(categoriesID: List<Int>, categoriesName: List<String>, selectedCategoryId: Int, onClick: (Int) -> Unit) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(categoriesID){id ->
                IconWithText(when(id){
                    1 ->R.drawable.map_spa
                    2 ->R.drawable.emojione_nail_polish
                    3 ->R.drawable.twemoji_man_getting_massage
                    4 ->R.drawable.goi_dau
                    5 ->R.drawable.twemoji_girl_light_skin_tone
                    else -> R.drawable.map_spa
                }
                    , categoriesName[id-1], id,id == selectedCategoryId){onClick(it)}
            }
        }
    }
}


@Composable
fun IconWithText(icon: Int, title: String, id: Int, isSelection: Boolean, onClick: (Int) -> Unit){
    Column(
        modifier = Modifier.padding(top = 15.dp)
            .width(70.dp)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(Color(if (isSelection) 0x80D9D9D9 else 0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton (
            onClick = { onClick(id) },
            modifier = Modifier
                .padding(7.dp)
                .size(48.dp) // kích thước tổng thể nút
                .clip(CircleShape) // bo tròn
                .background(Color(0xD9DBC37C)) // màu nền tùy ý (optional)
        ){
            Image(
                painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
fun itemCardService(services: List<Service>, title: String, img: Int, navController: NavController){
    val filteredServices = services.filter {
        when (title) {
            "Bán chạy nhất" -> it.visitors >= 900
            "Ưu đãi" -> it.discount > 0
            else -> it.discount <= 0 && it.visitors < 900
        }
    }
    if(filteredServices.isNotEmpty()){
        Card(
            modifier = Modifier.fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0x4C1E1E1E),
                    shape = RoundedCornerShape(15.dp) // phải giống shape của Card
                ),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    if (img != -1)
                        Image(
                            painter = painterResource(id = img),
                            contentDescription = ""
                        )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(max = 300.dp),
                ) {
                    items(filteredServices){service ->
                        itemDisplayService(service, service.id,navController)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun itemDisplayService(service: Service, serviceID: String, navController: NavController){
    Row (
        modifier = Modifier.fillMaxWidth()
            .padding(top = 15.dp)
            .clickable(onClick = {navController.navigate("ChiTietDichVu/$serviceID")})
    ) {
        AsyncImage(
            model = service.image,
            contentDescription ="",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = service.name,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "",
                    tint = Color(0xFF818181),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = " Lượt khách: ${service.visitors}",
                    color = Color(0xFF818181)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Đánh giá: ${service.rating}",
                    color = Color(0xFF818181)
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = Color(0xFFFFDC5D),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatCost(service.price),
                    style = TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = formatCost((100-service.discount)*service.price/100),
                    fontSize = 22.sp,
                    color = Color.Red
                )
            }
        }
    }
}


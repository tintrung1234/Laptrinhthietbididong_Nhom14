package com.example.spa_ower_app

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ThemSuaDichVu(
    navController: NavController,
    serviceViewModel: ServiceViewModel = viewModel()
) {
    var isThemDichVu by remember { mutableStateOf(true) }
    //Get info user
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userDocRef = currentUser?.let { firestore.collection("Users").document(it.uid) }

    // State cho thông tinS
    var admin by remember { mutableStateOf(0) }

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
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(79.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Thêm",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable(onClick = { isThemDichVu = true }),
                        textDecoration = if (isThemDichVu) TextDecoration.Underline else null
                    )
                    Text(
                        "Sửa",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clickable(onClick = { isThemDichVu = false }),
                        textDecoration = if (!isThemDichVu) TextDecoration.Underline else null
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { navController.navigate("TrangChu") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD9D9D9).copy(
                                alpha = 0.79f
                            )
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(55.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.vector4),
                            contentDescription = null,
                            modifier = Modifier.size(23.dp, 19.dp),
                            tint = Color.Black
                        )
                    }
                    Image(
                        painterResource(R.drawable.logo7),
                        contentDescription = null,
                        modifier = Modifier.size(79.dp)
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        if (currentUser != null && admin == 1) {
            if (isThemDichVu) {
                FormThemDichVu(navController)
            } else {
                FormSuaDichVu(navController)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(navController: NavController) {
    Column(
        modifier = Modifier.background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color(0xFFDBC37C), shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                        .padding(10.dp),
                ) {
                    Text(
                        "Thông Tin dịch vụ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painterResource(R.drawable.noto_label),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            "Tên dịch vụ",
                            modifier = Modifier.padding(horizontal = 7.dp),
                            fontSize = 13.sp
                        )
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )
                    }
                    //Drop box
                    var items2 = listOf("Massage xua tan nhức mỏi", "Ob2", "Ob3", "Ob4")
                    var expanded2 by remember { mutableStateOf(false) }
                    var selectedOption2 by remember { mutableStateOf(items2[0]) }

                    Row(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopStart)
                            .fillMaxWidth()
                            .border(
                                2.dp,
                                color = Color(0xFF544C4C24).copy(0.14f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(0.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded2,
                            onExpandedChange = { expanded2 = !expanded2 }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .padding(0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = selectedOption2,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded2
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .padding(0.dp), // Remove padding for TextField
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.White,
                                        focusedContainerColor = Color.White,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }

                            ExposedDropdownMenu(
                                expanded = expanded2,
                                onDismissRequest = { expanded2 = false },
                                modifier = Modifier.padding(0.dp) // Remove padding for dropdown menu
                            ) {
                                items2.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            selectedOption2 = selectionOption
                                            expanded2 = false
                                        },
                                        modifier = Modifier.padding(0.dp) // Remove padding for menu items
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painterResource(R.drawable.fxemoji_note),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            "Mô tả",
                            modifier = Modifier.padding(horizontal = 7.dp),
                            fontSize = 13.sp
                        )
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
                            .height(150.dp)
                            .padding(3.dp)
                            .background(
                                Color(0xFFD9D9D9).copy(0.25f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                color = Color.Black.copy(0.4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        var text = remember { mutableStateOf("") }
                        BasicTextField(
                            value = text.value,
                            onValueChange = { text.value = it },
                            textStyle = TextStyle(fontSize = 13.sp, color = Color.Black),
                            decorationBox = { innerTextField ->
                                if (text.value.isEmpty()) {
                                    Text(
                                        "• Thư giãn toàn thân – Giảm căng thẳng, mệt mỏi.\u2028• Bấm huyệt – Cải thiện tuần hoàn, giảm đau nhức.\u2028• Phục hồi năng lượng – Tăng cường sức khỏe, ngủ ngon hơn.",
                                        color = Color.Gray
                                    )
                                }
                                innerTextField()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painterResource(R.drawable.emojione_money_bag),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            "Giá dịch vụ",
                            modifier = Modifier.padding(horizontal = 7.dp),
                            fontSize = 13.sp
                        )
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )

                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .padding(3.dp)
                                .background(
                                    Color(0xFFD9D9D9).copy(0.25f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    color = Color.Black.copy(0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 4.dp)
                        ) {
                            var text = remember { mutableStateOf("") }
                            BasicTextField(
                                value = text.value,
                                onValueChange = { text.value = it },
                                textStyle = TextStyle(fontSize = 13.sp, color = Color.Black),
                                decorationBox = { innerTextField ->
                                    if (text.value.isEmpty()) {
                                        Text("400.000", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text(
                            "VNĐ",
                            modifier = Modifier.padding(horizontal = 7.dp),
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.height(5.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painterResource(R.drawable.fxemoji_hourglass),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            "Thời lượng",
                            modifier = Modifier.padding(horizontal = 4.dp),
                            fontSize = 13.sp
                        )
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .padding(3.dp)
                                .background(
                                    Color(0xFFD9D9D9).copy(0.25f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    color = Color.Black.copy(0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            var text = remember { mutableStateOf("") }
                            BasicTextField(
                                value = text.value,
                                onValueChange = { text.value = it },
                                textStyle = TextStyle(fontSize = 13.sp, color = Color.Black),
                                decorationBox = { innerTextField ->
                                    if (text.value.isEmpty()) {
                                        Text("300", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text("Phút", fontSize = 13.sp)
                    }

                    Spacer(Modifier.height(5.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Giảm giá", fontSize = 13.sp)
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .padding(3.dp)
                                .background(
                                    Color(0xFFD9D9D9).copy(0.25f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    color = Color.Black.copy(0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            var text = remember { mutableStateOf("") }
                            BasicTextField(
                                value = text.value,
                                onValueChange = { text.value = it },
                                textStyle = TextStyle(fontSize = 13.sp, color = Color.Black),
                                decorationBox = { innerTextField ->
                                    if (text.value.isEmpty()) {
                                        Text("300", color = Color.Gray)
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text("%", fontSize = 13.sp)
                    }

                    Spacer(Modifier.height(5.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.icon_park_view_list),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            "Loại dịch vụ",
                            modifier = Modifier.padding(horizontal = 4.dp),
                            fontSize = 13.sp
                        )
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )
                    }
                    //Drop box
                    var items = listOf("Massage", "Ob2", "Ob3", "Ob4")
                    var expanded by remember { mutableStateOf(false) }
                    var selectedOption by remember { mutableStateOf(items[0]) }

                    Row(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopStart)
                            .fillMaxWidth()
                            .border(
                                2.dp,
                                color = Color(0xFF544C4C24).copy(0.14f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(0.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .padding(0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
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
                                        .fillMaxWidth()
                                        .padding(0.dp), // Remove padding for TextField
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.White,
                                        focusedContainerColor = Color.White,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.padding(0.dp) // Remove padding for dropdown menu
                            ) {
                                items.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            selectedOption = selectionOption
                                            expanded = false
                                        },
                                        modifier = Modifier.padding(0.dp) // Remove padding for menu items
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.fluent_color_image_16),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            "Hình ảnh",
                            modifier = Modifier.padding(horizontal = 4.dp),
                            fontSize = 13.sp
                        )
                        Icon(
                            painterResource(R.drawable.important),
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = Color(0xFFDF2D2D)
                        )

                        Spacer(Modifier.width(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.attach_file),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                            Text("File", fontSize = 13.sp)
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .width(116.dp)
                            .border(
                                1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Image(
                            painterResource(R.drawable.khuyen_mai2),
                            contentDescription = null,
                            modifier = Modifier.size(116.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = { navController.navigate("TrangChu") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBC37C)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    Text("Lưu", fontSize = 14.sp)
                    Icon(
                        painterResource(R.drawable.ic_baseline_save_all),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(20.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun FormThemDichVu(navController: NavController) {
    // Them code xu li sau
    // ..............
    Form(navController)
}

@Composable
fun FormSuaDichVu(navController: NavController) {
    // Them code xu li sau
    // ..............
    Form(navController)
}
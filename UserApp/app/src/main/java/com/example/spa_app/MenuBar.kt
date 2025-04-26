package com.example.spa_app

import android.graphics.Color.rgb
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MenuBar(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val user = viewModel.authState

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth()
                .background(color = Color(rgb(219, 195, 124)))
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf(
                Triple(R.drawable.icon_trang_chu, "Trang chủ", "TrangChu"),
                Triple(R.drawable.icon_dat_lich, "Đặt lịch", "DatLich"),
                Triple(R.drawable.icon_lich_su,
                    "Lịch sử",
                    if (user != null) {
                        "LichSu"
                    } else {
                        "DangNhapDangKy"
                    }
                ),
                Triple(
                    R.drawable.icon_tai_khoan, "Tài khoản",
                    if (user != null) {
                        "TaiKhoan"
                    } else {
                        "DangNhapDangKy"
                    }
                ),
            ).forEach { (icon, title, route) ->
                val isSelected = currentRoute == route
                val interactionSource = remember { MutableInteractionSource() }
                val buttonPressColor by animateColorAsState(
                    if (isSelected) Color(rgb(140, 121, 63)) else Color.Transparent,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )

                Column(
                    modifier = Modifier.width(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(buttonPressColor, shape = RoundedCornerShape(30.dp))
                            .clickable(
                                onClick = {
                                    if (currentRoute != route) {
                                        navController.navigate(route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                        }
                                    }
                                },
                                interactionSource = interactionSource,
                                indication = null
                            )
                            .padding(25.dp, 9.dp)
                    ) {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp, 20.dp)
                        )
                    }
                    Text(title, fontSize = 12.sp, lineHeight = 14.sp)
                }
            }
        }
    }
}

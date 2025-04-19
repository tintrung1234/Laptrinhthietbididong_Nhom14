package com.example.spa_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spa_app.ui.theme.Spa_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Spa_appTheme {
                Controller()
            }
        }
    }
}

@Composable
fun Controller() {
    val navConTroller = rememberNavController()
    NavHost(navController = navConTroller, startDestination = "DangNhapDangKy") {
        composable("TrangChu") { TrangChu(navConTroller) }
        composable("DatLich") { Trangdatlich(navConTroller) }
        composable("DanhGia") { ReviewPage(navConTroller) }
        composable("ThongBao") { NotifyScreen(navConTroller) }
        composable("ChiTietDichVu") { DetailServiceScreen(navConTroller) }
        composable("LichSu") { HistoryScreen(navConTroller) }
        composable("MaGiamGia") { TrangMaGiamGia(navConTroller) }
        composable("DangNhapDangKy") { LoginRegisterScreen(navConTroller) }
        composable("LienHe") { TrangLienHe(navConTroller) }
        composable("ChiTietLichHen") { AppointmentDetailScreen(navConTroller) }
        composable("CacGoiDichVu") { ServiceScreen(navConTroller) }
        composable("TaiKhoan") { InforScreen(navConTroller) }
        composable("TrangThanhToan") { PaymentScreen(navConTroller) }
        composable("TrangDatLich") { Trangdatlich(navConTroller) }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    Spa_appTheme {
        Controller()
    }
}
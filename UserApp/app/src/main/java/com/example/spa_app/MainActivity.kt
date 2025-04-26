package com.example.spa_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spa_app.ui.theme.Spa_appTheme
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

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
    val servicesViewModel: ServiceViewModel = viewModel()
    val staffsViewModel: StaffViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()
    NavHost(navController = navConTroller, startDestination = "TrangChu") {
        composable("TrangChu") { TrangChu(navConTroller, servicesViewModel, staffsViewModel) }
        composable("DatLich") { Trangdatlich(navConTroller) }
        composable("DanhGia") { ReviewPage(navConTroller) }
        composable("ThongBao") { NotifyScreen(navConTroller) }
        composable("ChiTietDichVu") { DetailServiceScreen(navConTroller) }
        composable("LichSu") { HistoryScreen(navConTroller,appointmentViewModel) }
        composable("MaGiamGia") { TrangMaGiamGia(navConTroller) }
        composable("DangNhapDangKy") { LoginRegisterScreen(navConTroller) }
        composable("LienHe") { TrangLienHe(navConTroller) }
        composable("ChiTietLichHen") { AppointmentDetailScreen(navConTroller) }
        composable("CacGoiDichVu") { ServiceScreen(navConTroller, servicesViewModel, categoryViewModel) }
        composable("TaiKhoan") { InforScreen(navConTroller) }
        composable("TrangThanhToan") { PaymentScreen(navConTroller) }
        composable("TrangDatLich") { Trangdatlich(navConTroller) }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview() {
//    Spa_appTheme {
////        Controller()
//    val navConTroller = rememberNavController()
//    LoginRegisterScreen(navConTroller)
//    }
//}
package com.example.spa_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    val servicesViewModel: ServiceViewModel = viewModel()
    val staffsViewModel: StaffViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()
    NavHost(navController = navConTroller, startDestination = "TrangChu") {
        composable("TrangChu") { TrangChu(navConTroller, servicesViewModel, staffsViewModel) }
        composable("DatLich") { TrangDatLich(navConTroller) }
        composable("DanhGia") { ReviewPage(navConTroller) }
        composable("ThongBao") { NotifyScreen(navConTroller) }
        composable(
            "ChiTietDichVu/{serviceId}",
            arguments = listOf(navArgument("serviceId") { type = NavType.IntType })
        ) {backStackEntry ->
            val serviceId = backStackEntry.arguments?.getInt("serviceId")
            DetailServiceScreen(navConTroller, serviceId)
        }
        composable("LichSu") { HistoryScreen(navConTroller,appointmentViewModel) }
        composable("MaGiamGia") { TrangMaGiamGia(navConTroller) }
        composable("DangNhapDangKy") { LoginRegisterScreen(navConTroller) }
        composable("LienHe") { TrangLienHe(navConTroller) }
        composable(
            "ChiTietLichHen/{appointmentId}",
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) {backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")
            AppointmentDetailScreen(navConTroller, appointmentId) }
        composable("CacGoiDichVu") { ServiceScreen(navConTroller, servicesViewModel, categoryViewModel) }
        composable("TaiKhoan") { InforScreen(navConTroller) }
        composable("TrangThanhToan") { PaymentScreen(navConTroller) }
        composable("TrangDatLich") { TrangDatLich(navConTroller) }
        composable("TimKiem") { Search(navConTroller, servicesViewModel) }
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
package com.example.spa_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spa_app.ui.theme.Spa_appTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var notifyViewModel: NotifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        notifyViewModel = ViewModelProvider(this)[NotifyViewModel::class.java]

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        // Luôn chạy khi app còn foreground, không bị recompose
        lifecycleScope.launch {
            if (userId != null) {
                notifyViewModel.loadUserNotifications(userId)
            }
        }

        val startDestination = when (intent?.getStringExtra("navigate_to")) {
            "lichsu" -> "LichSu"
            "taikhoan" -> "TaiKhoan"
            else -> "TrangChu"
        }

        setContent {
            Spa_appTheme {
                Controller(startDestination = startDestination)
            }
        }
    }
}

@Composable
fun Controller(startDestination: String = "TrangChu") {
    val navConTroller = rememberNavController()
    val servicesViewModel: ServiceViewModel = viewModel()
    val staffsViewModel: StaffViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val discountViewModel: DiscountViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()
    //TrangThanhToan/1TCPNApprFwQoorJiMtE
    NavHost(navController = navConTroller, startDestination = startDestination) {
        composable("TrangChu") { TrangChu(navConTroller, servicesViewModel, staffsViewModel) }
        composable("DatLich") { TrangDatLich(navConTroller) }
        composable("DanhGia") { ReviewPage(navConTroller) }
        composable("ThongBao") { NotifyScreen(navConTroller) }
        composable(
            "ChiTietDichVu/{serviceId}",
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            DetailServiceScreen(navConTroller, serviceId, servicesViewModel, categoryViewModel)
        }
        composable("LichSu") { HistoryScreen(navConTroller, appointmentViewModel) }
        composable("MaGiamGia") { TrangMaGiamGia(navConTroller, discountViewModel) }
        composable("DangNhapDangKy") { LoginRegisterScreen(navConTroller) }
        composable("LienHe") { TrangLienHe(navConTroller) }
        composable(
            "ChiTietLichHen/{appointmentId}",
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")
            AppointmentDetailScreen(
                navConTroller,
                appointmentId,
                appointmentViewModel,
                servicesViewModel,
                categoryViewModel,
                staffsViewModel
            )
        }
        composable("CacGoiDichVu") {
            ServiceScreen(
                navConTroller,
                servicesViewModel,
                categoryViewModel
            )
        }
        composable("TaiKhoan") { InforScreen(navConTroller) }
        composable(
            "TrangThanhToan/{appointmentId}",
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")
            PaymentScreen(navConTroller, appointmentId, appointmentViewModel, servicesViewModel, discountViewModel)
        }
        composable("TrangDatLich") { TrangDatLich(navConTroller) }
        composable("TimKiem") { Search(navConTroller, servicesViewModel) }
    }
}
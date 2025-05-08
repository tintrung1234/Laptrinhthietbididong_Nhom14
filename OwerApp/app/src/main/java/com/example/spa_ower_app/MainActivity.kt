package com.example.spa_ower_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spa_ower_app.ThemSuaDichVu
import com.example.spa_ower_app.ui.theme.Spa_Ower_AppTheme
import kotlinx.coroutines.launch
import java.time.format.TextStyle


class MainActivity : ComponentActivity() {
    private lateinit var notifyViewModel: NotifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        notifyViewModel = ViewModelProvider(this)[NotifyViewModel::class.java]

        // Luôn chạy khi app còn foreground, không bị recompose
        lifecycleScope.launch {
            notifyViewModel.notificationEvent.collect { notification ->
                showPopupNotification(this@MainActivity, notification.contentForOwner)
            }
        }

        val route = intent?.getStringExtra("navigate_to") ?: "TrangChu"

        setContent {
            Spa_Ower_AppTheme {
                Controller(startDestination = route)
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
    val appointmentViewModel: AppointmentViewModel = viewModel()
    NavHost(navController = navConTroller, startDestination = startDestination) {
        composable("TrangChu") { TrangChu(navConTroller, servicesViewModel, staffsViewModel, ) }
        composable("DanhGia") { ReviewPage(navConTroller) }
        composable("ThongBao") { NotifyScreen(navConTroller) }
        composable("ThemSuaDichVu") { ThemSuaDichVu(navConTroller, servicesViewModel) }
        composable(
            "ChiTietDichVu/{serviceId}",
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) {backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            DetailServiceScreen(navConTroller, serviceId, servicesViewModel, categoryViewModel)
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
            AppointmentDetailScreen(navConTroller, appointmentId, appointmentViewModel, servicesViewModel, categoryViewModel,staffsViewModel) }
        composable("CacGoiDichVu") { ServiceScreen(navConTroller, servicesViewModel, categoryViewModel) }
        composable("TaiKhoan") { InforScreen(navConTroller) }
        composable("DoanhThu") { RevenueScreen(navConTroller) }

        composable("TimKiem") { Search(navConTroller, servicesViewModel) }
    }
}
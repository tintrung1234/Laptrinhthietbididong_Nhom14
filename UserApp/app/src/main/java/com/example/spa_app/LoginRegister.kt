package com.example.spa_app

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

@Composable
fun LoginRegisterScreen(navController: NavController) {
    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(color = Color.White)
        ) {
            Spacer(Modifier.height(15.dp))
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

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.logo7),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Đăng Nhập",
                    fontSize = 18.sp,
                    fontWeight = if (isLogin) FontWeight.Bold else FontWeight.Normal,
                    textDecoration = if (isLogin) TextDecoration.Underline else null,
                    modifier = Modifier
                        .clickable { isLogin = true }
                        .padding(end = 16.dp)
                )
                Text(
                    text = "Đăng Ký",
                    fontSize = 18.sp,
                    fontWeight = if (!isLogin) FontWeight.Bold else FontWeight.Normal,
                    textDecoration = if (!isLogin) TextDecoration.Underline else null,
                    modifier = Modifier.clickable { isLogin = false }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            val viewModel: AuthViewModel = viewModel()
            if (isLogin) {
                LoginForm(navController, viewModel)
            } else {
                RegisterForm(navController, viewModel)
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    // Ví dụ: ít nhất 6 ký tự
    return password.length >= 6
}

fun isValidPhoneNumber(phone: String): Boolean {
    val regex = Regex("^0\\d{9}$") // Bắt đầu bằng 0, theo sau là đúng 9 số
    return regex.matches(phone) && phone.isNotEmpty()
}

fun isValidName(name: String): Boolean {
    return name.trim().isNotEmpty()
}

@Composable
fun LoginForm(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val loginState by viewModel.loginState.collectAsState()

    // Nếu đăng nhập thành công -> Navigate
    LaunchedEffect(loginState) {
        loginState?.let { result ->
            if (result.isSuccess) {
                navController.navigate("TrangChu") {
                    popUpTo("LoginScreen") { inclusive = true }
                }
            } else if (result.isFailure) {
                error = "Tài khoản hoặc mật khẩu chưa chính xác"
            }
        }
    }

    Column {
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = null
            },
            label = { Text("Nhập Email") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("Email") == true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = null
            },
            label = { Text("Mật khẩu") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = R.drawable.iconpasseye),
                        contentDescription = "Eye icon"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("Mật khẩu") == true
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (error != null) {
            Text(text = error!!, color = Color.Red, modifier = Modifier.padding(top = 4.dp) .align(alignment = Alignment.CenterHorizontally))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (!isValidEmail(email)) {
                    error = "Email không hợp lệ"
                } else if (!isValidPassword(password)) {
                    error = "Mật khẩu phải từ 6 ký tự trở lên"
                } else {
                    viewModel.signIn(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFEFDCA9))
        ) {
            Text("Đăng nhập", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(14.dp))
        GoogleLoginButton(
            onSuccess = { user ->
                navController.navigate("TrangChu")
            },
            onError = { error ->
                Log.e("Login", "Error: ${error.message}")
            }
        )

        Text(
            text = "Quên mật khẩu",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
                .clickable(onClick = { navController.navigate("QuenMatKhau") }),
            fontSize = 12.sp,
            color = Color.Black,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun RegisterForm(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    Column {
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                error = null
            },
            label = { Text("Tên người dùng *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("Tên") == true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                error = null
            },
            label = { Text("Số điện thoại *") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("Số điện thoại") == true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = null
            },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("Email") == true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = null
            },
            label = { Text("Mật khẩu *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = R.drawable.iconpasseye),
                        contentDescription = "Eye icon"
                    )

                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("Mật khẩu") == true
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (error != null) {
            Text(
                text = error!!,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }

        if (message != null) {
            Text(
                text = message!!,
                color = Color.Green,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (!isValidName(username)) {
                    error = "Tên không được để trống"
                } else if (!isValidPhoneNumber(phone)) {
                    error = "Số điện thoại không hợp lệ"
                } else if (!isValidEmail(email)) {
                    error = "Email không hợp lệ"
                } else if (!isValidPassword(password)) {
                    error = "Mật khẩu phải từ 6 ký tự trở lên"
                } else {
                    viewModel.signUpWithDetails(email, password, username, phone)
                    message = "Đăng ký tài khoản thành công"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFEFDCA9))
        ) {
            Text("Đăng ký", color = Color.Black)
        }
    }
}

@Composable
fun GoogleLoginButton(
    onSuccess: (FirebaseUser) -> Unit,
    onError: (Exception) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            Firebase.auth.signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    onSuccess(authResult.user!!)
                }
                .addOnFailureListener {
                    onError(it)
                }
        } catch (e: ApiException) {
            onError(e)
        }
    }

    Button(
        onClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val client = GoogleSignIn.getClient(context, gso)

            client.signOut().addOnCompleteListener {
                launcher.launch(client.signInIntent)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "Google",
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text("Đăng nhập với Google")
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        TopLayout("Quên mật khẩu", { navController.navigate("DangNhapDangKy") })
        Text("Quên mật khẩu", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))

        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = {
                if (email.isBlank()) {
                    message = "Vui lòng nhập email."
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                message = "Đã gửi email reset mật khẩu! Kiểm tra hộp thư."
                            } else {
                                val exception = task.exception
                                if (exception is FirebaseAuthInvalidUserException) {
                                    message = "Email này chưa đăng ký."
                                } else {
                                    message = "Có lỗi xảy ra: ${exception?.message}"
                                }
                            }
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFEFDCA9)),
        ) {
            Text("Gửi link reset mật khẩu")
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (message.isNotEmpty()) {
            Text(text = message, color = Color.Red)
        }
    }
}


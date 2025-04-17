package com.example.spa_ower_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TopLayout(title: String, onclick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Mã giảm giá",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 28.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onclick,
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
                    painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(79.dp)
                )
            }
        }
    }
}
package com.example.lab1.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lab1.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    vm: WelcomeViewModel,
    onNavigateToMain: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    val fernando = FontFamily(
        Font(R.font.fernando, FontWeight.Normal)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // ===== BACKGROUND =====
        Image(
            painter = painterResource(R.drawable.intro_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ===== NAME BOX =====
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 300.dp)
                .fillMaxWidth(0.95f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.namebox),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )

            BasicTextField(
                value = name,
                onValueChange = {
                    if (it.length <= 20) name = it
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(0xFF7A2F3F),
                    fontSize = 22.sp,
                    fontFamily = fernando,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 8.dp)
            )
        }

        // ===== ENTER BUTTON =====
        Image(
            painter = painterResource(R.drawable.enter),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
                .width(180.dp)
                .clickable {
                    if (name.isNotBlank()) {
                        vm.createCharacter(name, onNavigateToMain)
                    }
                }
        )
    }
}
package com.example.lab1.ui.screen.timer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab1.R
import com.example.lab1.ui.screen.main.Background
import com.example.lab1.ui.screen.main.MainViewModel

val jersey20 = FontFamily(
    Font(R.font.jersey20, FontWeight.Normal)
)

val pixelify = FontFamily(
    Font(R.font.pixelify, FontWeight.Normal)
)

@Composable
fun TimerScreen(
    vm: MainViewModel,
    onBack: () -> Unit
) {
    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(25) }

    // ================= LOGIC =================

    fun increaseMinute() {
        if (hour == 5 && minute == 59) return

        minute += 1

        if (minute >= 60) {
            minute = 0
            hour = (hour + 1).coerceAtMost(5)
        }
    }

    fun decreaseMinute() {
        if (hour == 0 && minute <= 1) {
            minute = 1
            return
        }

        minute -= 1

        if (minute < 0) {
            if (hour > 0) {
                hour -= 1
                minute = 59
            } else {
                minute = 1
            }
        }
    }

    fun increaseHour() {
        if (hour < 5) hour += 1
    }

    fun decreaseHour() {
        if (hour > 0) {
            hour -= 1
            if (hour == 0 && minute == 0) {
                minute = 1
            }
        }
    }

    val totalMinutes = hour * 60 + minute

    // ================= UI =================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Background()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.5f)
                .align(Alignment.Center)
        ) {

            Image(
                painter = painterResource(R.drawable.background2),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            Image(
                painter = painterResource(R.drawable.exit_btn),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 24.dp, top = 112.dp)
                    .size(42.dp)
                    .clickable { onBack() }
            )

            Text(
                text = "Cultivate",
                fontSize = 56.sp,
                fontFamily = jersey20,
                color = Color(0xFF7A2F3F),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 120.dp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TimeColumn(
                    value = hour,
                    onIncrease = { increaseHour() },
                    onDecrease = { decreaseHour() }
                )

                Spacer(Modifier.width(12.dp))

                Image(
                    painter = painterResource(R.drawable.two_dots),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(Modifier.width(12.dp))

                TimeColumn(
                    value = minute,
                    onIncrease = { increaseMinute() },
                    onDecrease = { decreaseMinute() }
                )
            }

            Image(
                painter = painterResource(R.drawable.start_timer),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
                    .fillMaxWidth(0.8f)
                    .clickable {
                        if (totalMinutes == 0) return@clickable
                        vm.startSession(totalMinutes * 60L)
                        onBack()
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun TimeColumn(
    value: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.up_arrow),
            contentDescription = null,
            modifier = Modifier
                .size(74.dp)
                .clickable { onIncrease() }
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(100.dp)
                .aspectRatio(1.2f),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.drawable.timer_box),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = String.format("%02d", value),
                fontSize = 42.sp,
                fontFamily = jersey20,
                color = Color(0xFF5A2E2E),
                modifier = Modifier.offset(y = (-2).dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Image(
            painter = painterResource(R.drawable.down_arrow),
            contentDescription = null,
            modifier = Modifier
                .size(74.dp)
                .clickable { onDecrease() }
        )
    }
}
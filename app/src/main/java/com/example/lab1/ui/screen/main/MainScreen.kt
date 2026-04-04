package com.example.lab1.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
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
import com.example.lab1.domain.rules.RealmConfig
import com.example.lab1.ui.screen.timer.jersey20
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun MainScreen(vm: MainViewModel, onNavigateToSetup: () -> Unit) {
    val character by vm.character.collectAsState(initial = null)
    val expUi by vm.expUi.collectAsState()

    if (character == null) return

    val realm = RealmConfig.fromOrdinal(character!!.realmOrdinal)
    val isMeditating = character!!.isMeditating

    Box(modifier = Modifier.fillMaxSize()) {

        Background()

        ExpBar(
            modifier = Modifier.align(Alignment.TopCenter),
            realmName = realm.realmName,
            current = expUi.current,
            required = expUi.required,
            progress = expUi.progress
        )

        TopActions(
            modifier = Modifier.fillMaxSize()
        )

        CharacterStage(
            modifier = Modifier.align(Alignment.Center),
            isMeditating = isMeditating
        )

        CultivateButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            isMeditating = isMeditating,
            onClick = {
                if (isMeditating) vm.stopSession()
                else onNavigateToSetup()
            }
        )
    }
}

@Composable
fun Background() {
    Image(
        painter = painterResource(R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ExpBar(
    realmName: String,
    current: Long,
    required: Long,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 24.dp, end = 24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(realmName, fontSize = 24.sp, fontFamily = jersey20, color = Color(0xFFFFD700))
            Text("$current / $required", fontSize = 12.sp, color = Color.White)
        }

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color.White.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(Color(0xFF4CAF50))
            )
        }
    }
}

@Composable
fun TopActions(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 120.dp, start = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.history_btn),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )

            Image(
                painter = painterResource(R.drawable.inventory_btn),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )
        }

        Image(
            painter = painterResource(R.drawable.realmbreak_btn),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 140.dp, end = 24.dp)
                .size(70.dp)
        )
    }
}

@Composable
fun CharacterStage(
    modifier: Modifier = Modifier,
    isMeditating: Boolean
) {

    Box(
        modifier = modifier.size(350.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        Image(
            painter = painterResource(
                id = if (isMeditating) R.drawable.sit else R.drawable.stand
            ),
            contentDescription = null,
            modifier = Modifier.height(220.dp)
        )

        Timer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp),
            isRunning = isMeditating,
            totalSeconds = 25 * 60 // hoặc lấy từ ViewModel sau
        )
    }
}

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    totalSeconds: Int = 1500
) {

    val jersey20 = FontFamily(
        Font(R.font.jersey20, FontWeight.Normal)
    )
    var remaining by remember { mutableIntStateOf(totalSeconds) }

    LaunchedEffect(isRunning, totalSeconds) {
        remaining = if (isRunning) totalSeconds else 0

        if (isRunning) {
            while (remaining > 0) {
                delay(1000)
                remaining--
            }
        }
    }

    val hours = remaining / 3600
    val minutes = (remaining % 3600) / 60

    val timeText = String.format(
        Locale.US,
        "%02d:%02d",
        hours,
        minutes
    )

    Text(
        text = timeText,
        modifier = modifier,
        fontSize = 82.sp,
        fontFamily = jersey20,
        color = Color.White,
        letterSpacing = 2.sp,
    )
}

@Composable
fun CultivateButton(
    modifier: Modifier = Modifier,
    isMeditating: Boolean,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(
            id = if (isMeditating) R.drawable.stop else R.drawable.start
        ),
        contentDescription = null,
        modifier = modifier
            .padding(bottom = 80.dp)
            .clickable { onClick() }
    )
}
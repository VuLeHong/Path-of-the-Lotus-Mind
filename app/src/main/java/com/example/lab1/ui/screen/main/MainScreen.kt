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
import androidx.compose.ui.platform.LocalView
import com.example.lab1.R
import com.example.lab1.domain.rules.OrbType.Companion.getOrbImage
import com.example.lab1.domain.rules.RealmConfig
import com.example.lab1.ui.screen.timer.jersey20
import com.example.lab1.ui.screen.timer.pixelify
import kotlinx.coroutines.delay
import java.util.Locale


@Composable
fun MainScreen(
    vm: MainViewModel,
    onNavigateToSetup: () -> Unit
) {
    val character by vm.character.collectAsState(initial = null)
    val expUi by vm.expUi.collectAsState()
    val duration by vm.sessionDuration.collectAsState()
    val startTime by vm.startTime.collectAsState()
    val reward by vm.reward.collectAsState()
    var showStopConfirm by remember { mutableStateOf(false) }

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
        if (isMeditating) {
            KeepScreenOn()
        }
        CharacterStage(
            modifier = Modifier.align(Alignment.Center),
            isMeditating = isMeditating,
            duration = duration,
            startTime = startTime,
            onFinish = {
                vm.completeSession(
                    startTime = startTime,
                    targetSeconds = duration,
                    elapsedSeconds = duration,
                    isSuccess = true
                )
            }
        )

        CultivateButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            isMeditating = isMeditating,
            onClick = {
                if (isMeditating) {
                    showStopConfirm = true
                } else {
                    onNavigateToSetup()
                }
            }
        )

        // ================= POPUP =================
        reward?.let {
            RewardPopup(
                reward = it,
                onReceive = { vm.clearReward() }
            )
        }

        if (showStopConfirm) {
            StopConfirmPopup(
                onYes = {
                    showStopConfirm = false

                    val elapsed = ((System.currentTimeMillis() - startTime) / 1000)

                    vm.completeSession(
                        startTime = startTime,
                        targetSeconds = duration,
                        elapsedSeconds = elapsed,
                        isSuccess = false
                    )
                },
                onNo = {
                    showStopConfirm = false
                }
            )
        }
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
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 24.dp, end = 24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(realmName, fontSize = 24.sp, fontFamily = pixelify, color = Color(0xFFFFD700))
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
    isMeditating: Boolean,
    duration: Long,
    startTime: Long,
    onFinish: () -> Unit
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
            totalSeconds = duration.toInt(),
            startTime = startTime,
            onFinish = onFinish
        )
    }
}

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    totalSeconds: Int,
    startTime: Long,
    onFinish: () -> Unit
) {
    val jersey20 = FontFamily(
        Font(R.font.jersey20, FontWeight.Normal)
    )


    var remaining by remember { mutableIntStateOf(totalSeconds) }

    LaunchedEffect(isRunning, startTime, totalSeconds) {

        if (!isRunning) {
            remaining = 0
            return@LaunchedEffect
        }

        if (totalSeconds > 0) {
            while (true) {
                val elapsed = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                val newRemaining = (totalSeconds - elapsed).coerceAtLeast(0)
                remaining = newRemaining
                if (newRemaining == 0) {
                    onFinish()
                    break
                }
                delay(1000)
            }
        }
    }

    val hours = remaining / 3600
    val minutes = ((remaining % 3600) + 59) / 60

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
        color = Color.White
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

@Composable
fun RewardPopup(
    reward: RewardUi,
    onReceive: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f)),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(0.6f)
        ) {

            // ===== BG =====
            if(reward.isSuccess){
                Image(
                    painter = painterResource(R.drawable.bg_success),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            else {
                Image(
                    painter = painterResource(R.drawable.bg_fail),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.8f),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(60.dp))

                // ===== TITLE =====
                if (reward.isSuccess) {
                    Text(
                        text = "Session",
                        fontSize = 40.sp,
                        fontFamily = pixelify,
                        color = Color(0xFF7A2F3F)
                    )

                    Text(
                        text = "Complete!",
                        fontSize = 44.sp,
                        fontFamily = pixelify,
                        color = Color(0xFF2E8B57)
                    )
                } else {
                    Text(
                        text = "Session Failed",
                        fontSize = 44.sp,
                        fontFamily = pixelify,
                        color = Color(0xFFBD1025)
                    )
                }


                Spacer(Modifier.height(24.dp))

                if (!reward.isSuccess) {
                    Text(
                        text = reward.penalty ?: "",
                        fontSize = 20.sp,
                        fontFamily = pixelify,
                        color = Color(0xFFB63636)
                    )
                }else {
                    // ===== EXP =====
                    RewardItemImage(
                        icon = R.drawable.exp,
                        value = reward.exp,
                        label = "EXP"
                    )

                    // ===== ORBS =====
                    reward.orbs.forEach { (orb, count) ->
                        RewardItemImage(
                            icon = getOrbImage(orb),
                            value = count,
                            label = orb.displayName
                        )
                    }

                }

                Spacer(Modifier.weight(1f))
                // ===== BUTTON =====
                if (reward.isSuccess) {
                    Image(
                        painter = painterResource(R.drawable.receive_btn),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(bottom = 32.dp)
                            .clickable { onReceive() },
                        contentScale = ContentScale.Fit
                    )
                }
                else {
                    Image(
                        painter = painterResource(R.drawable.receive_btn),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(bottom = 200.dp)
                            .clickable { onReceive() },
                        contentScale = ContentScale.Fit
                    )
                }

            }
        }
    }
}

@Composable
fun RewardItemImage(
    icon: Int,
    value: Int,
    label: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // item box
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.drawable.item_box),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )

            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            fontFamily = jersey20,
            color = Color(0xFF7A2F3F)
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = "+$value",
            fontSize = 28.sp,
            fontFamily = jersey20,
            color = Color(0xFF7A2F3F)
        )

    }
}

@Composable
fun StopConfirmPopup(
    onYes: () -> Unit,
    onNo: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f)),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(0.7f)
        ) {

            Image(
                painter = painterResource(R.drawable.bg_success),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(40.dp))

                Text(
                    text = "Cultivation",
                    fontSize = 36.sp,
                    fontFamily = pixelify,
                    color = Color(0xFF7A2F3F)
                )

                Text(
                    text = "Incomplete",
                    fontSize = 36.sp,
                    fontFamily = pixelify,
                    color = Color(0xFFB23A3A)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Are you willing to\nlose your progress?",
                    fontSize = 20.sp,
                    fontFamily = pixelify,
                    color = Color(0xFF3A2E2E)
                )

                Spacer(Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.yes_btn),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onYes() }
                    )

                    Image(
                        painter = painterResource(R.drawable.no_btn),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNo() }
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun KeepScreenOn() {
    val view = LocalView.current

    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
        }
    }
}
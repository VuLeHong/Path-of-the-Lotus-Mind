package com.example.lab1.ui.screen.realmbreak

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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab1.R
import com.example.lab1.domain.rules.OrbType
import com.example.lab1.domain.rules.RealmConfig
import com.example.lab1.ui.screen.main.Background
import com.example.lab1.ui.screen.main.MainViewModel
import com.example.lab1.ui.screen.timer.jersey20
import com.example.lab1.ui.screen.timer.pixelify

// =====================
// 🔥 EXTENSION: convert weight → %
// =====================
fun Map<OrbType, Int>.toRatePercent(): Map<OrbType, Int> {
    val total = values.sum().toFloat()
    return mapValues { (_, v) ->
        ((v / total) * 100).toInt()
    }
}

@Composable
fun RealmBreakScreen(
    vm: MainViewModel,
    onBack: () -> Unit
) {

    val info by vm.breakthroughInfo.collectAsState()
    val expUi by vm.expUi.collectAsState()
    val character by vm.character.collectAsState()

    val currentXp = expUi.current
    val requiredXp = expUi.required

    // 🔥 current realm (QUAN TRỌNG)
    val currentRealm = character?.let {
        RealmConfig.fromOrdinal(it.realmOrdinal)
    }

    // 🔥 drop rate từ CURRENT REALM
    val dropRates = currentRealm
        ?.gachaPool
        ?.toRatePercent()
        ?: emptyMap()

    LaunchedEffect(Unit) {
        vm.loadBreakthrough()
    }

    Box(Modifier.fillMaxSize()) {

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
                    .padding(top = 110.dp, end = 24.dp)
                    .size(42.dp)
                    .clickable { onBack() }
            )

            Text(
                text = "Realm",
                fontSize = 56.sp,
                fontFamily = pixelify,
                color = Color(0xFF7A2F3F),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 120.dp)
            )

            info?.let { data ->

                val target = data.targetRealm

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 180.dp, start = 24.dp, end = 24.dp, bottom = 32.dp)
                ) {

                    // ===== REALM INFO =====
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {

                        Image(
                            painter = painterResource(R.drawable.realm_frame),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.FillBounds
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {

                            Text(
                                text = "Next Realm: ${target.realmName}",
                                fontSize = 14.sp,
                                fontFamily = pixelify,
                                color = Color(0xFF2E8B57)
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = target.description,
                                fontSize = 12.sp,
                                fontFamily = jersey20,
                                color = Color(0xFF5A2E2E)
                            )

                            Spacer(Modifier.weight(1f))

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Text(
                                    text = "Reward: ${target.auraName}",
                                    fontSize = 12.sp,
                                    fontFamily = pixelify,
                                    fontWeight = Medium,
                                    color = Color(0xFF7A2F3F)
                                )

                                Spacer(Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier.size(36.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.realm_box),
                                        contentDescription = null,
                                        modifier = Modifier.matchParentSize()
                                    )

                                    Image(
                                        painter = painterResource(target.auraSymbol),
                                        contentDescription = null,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // ===== REQUIREMENTS =====
                    Text(
                        text = "Requirements",
                        fontSize = 36.sp,
                        fontFamily = pixelify,
                        fontWeight = Bold,
                        color = Color(0xFF7A2F3F),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // ===== EXP =====
                        RequirementItem(
                            icon = R.drawable.exp,
                            label = "EXP",
                            current = currentXp.toInt(),
                            required = requiredXp.toInt(),
                            rate = 100
                        )

                        // ===== ORBS =====
                        target.orbRequirements.forEach { req ->

                            val missing = data.missingOrbs
                                .find { it.first == req.orbType }
                                ?.second ?: 0

                            val current = req.quantity - missing

                            val rate = dropRates[req.orbType] ?: 0

                            RequirementItem(
                                icon = OrbType.getOrbImage(req.orbType),
                                label = req.orbType.displayName,
                                current = current,
                                required = req.quantity,
                                rate = rate
                            )
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    Image(
                        painter = painterResource(
                            if (data.canBreak)
                                R.drawable.advance_btn
                            else
                                R.drawable.advance_grey_btn
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .align(Alignment.CenterHorizontally)
                            .clickable(enabled = data.canBreak) {
                                vm.doBreakthrough()
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun RequirementItem(
    icon: Int,
    label: String,
    current: Int,
    required: Int,
    rate: Int
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.item_realm),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )

            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {

            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = pixelify,
                color = Color(0xFF7A2F3F)
            )

            // 🔥 drop rate
            Text(
                text = "Drop rate: $rate%",
                fontSize = 12.sp,
                fontFamily = jersey20,
                color = Color(0xFF5A2E2E)
            )

            // 🔥 progress thật
            Text(
                text = "$current / $required",
                fontSize = 18.sp,
                fontFamily = jersey20,
                color = if (current >= required)
                    Color(0xFF2E8B57)
                else
                    Color(0xFFB63636)
            )
        }
    }
}
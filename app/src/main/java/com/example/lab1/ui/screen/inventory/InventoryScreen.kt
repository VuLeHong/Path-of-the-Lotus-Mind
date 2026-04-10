package com.example.lab1.ui.screen.inventory

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab1.R
import com.example.lab1.domain.rules.OrbType
import com.example.lab1.domain.rules.OrbType.Companion.getOrbImage
import com.example.lab1.ui.screen.main.Background
import com.example.lab1.ui.screen.main.MainViewModel
import com.example.lab1.ui.screen.timer.jersey20
import com.example.lab1.ui.screen.timer.pixelify

@Composable
fun InventoryScreen(
    vm: MainViewModel,
    onBack: () -> Unit
) {
    val orbMap by vm.inventory.collectAsState(
        initial = emptyMap()
    )
    val visibleOrbs = orbMap
        .filter { it.value > 0 }
        .toList()
        .sortedBy { it.first.ordinal }

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

            // ===== BACKGROUND =====
            Image(
                painter = painterResource(R.drawable.background2),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            // ===== EXIT BUTTON =====
            Image(
                painter = painterResource(R.drawable.exit_btn),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 110.dp, end = 24.dp)
                    .size(42.dp)
                    .clickable { onBack() }
            )

            // ===== TITLE =====
            Text(
                text = "Inventory",
                fontSize = 56.sp,
                fontFamily = pixelify,
                color = Color(0xFF7A2F3F),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 32.dp, top = 120.dp)
            )

            // ===== CONTENT =====
            if (visibleOrbs.isEmpty()) {

                Text(
                    text = "No items",
                    fontSize = 28.sp,
                    fontFamily = pixelify,
                    color = Color(0xFF5A2E2E),
                    modifier = Modifier.align(Alignment.Center)
                )

            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 220.dp,
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 40.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    items(visibleOrbs) { (orb, count) ->

                        InventoryItem(
                            orb = orb,
                            count = count
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryItem(
    orb: OrbType,
    count: Int
) {

    Box(
        modifier = Modifier.aspectRatio(1f).padding(start = 10.dp)
    ) {

        // ===== BOX =====
        Image(
            painter = painterResource(R.drawable.inventory_box),
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            // ===== ORB (TOP RIGHT) =====
            Image(
                painter = painterResource(getOrbImage(orb)),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center)
                    .padding(top = 6.dp, end = 6.dp)
            )

            // ===== COUNT (UNDER ORB) =====
            Text(
                text = "x$count",
                fontSize = 18.sp,
                fontFamily = jersey20,
                color = Color(0xFF2E2E2E),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 70.dp, end = 10.dp)
            )
        }
        // ===== NAME (BOTTOM CENTER) =====
        Text(
            text = orb.displayName,
            fontSize = 16.sp,
            fontFamily = pixelify,
            color = Color(0xFF2E2E2E),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 12.dp)
        )
    }
}
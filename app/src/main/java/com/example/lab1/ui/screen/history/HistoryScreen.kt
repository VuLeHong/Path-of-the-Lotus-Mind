package com.example.lab1.ui.screen.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab1.R
import com.example.lab1.ui.screen.main.Background
import com.example.lab1.ui.screen.main.HistoryItemUi
import com.example.lab1.ui.screen.main.MainViewModel
import com.example.lab1.ui.screen.timer.jersey20
import com.example.lab1.ui.screen.timer.pixelify

@Composable
fun HistoryScreen(
    vm: MainViewModel,
    onBack: () -> Unit
) {

    // ===== STATE =====
    val history by vm.history.collectAsState()

    // ===== LOAD DATA =====
    LaunchedEffect(Unit) {
        vm.loadHistory()
    }

    Box(Modifier.fillMaxSize()) {

        Background()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.5f)
                .align(Alignment.Center)
        ) {

            // ===== BG =====
            Image(
                painter = painterResource(R.drawable.background2),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            // ===== BACK =====
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
                text = "History",
                fontSize = 56.sp,
                fontFamily = pixelify,
                color = Color(0xFF7A2F3F),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 120.dp)
            )

            // ===== EMPTY STATE =====
            if (history.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No sessions yet",
                        fontSize = 18.sp,
                        fontFamily = pixelify,
                        color = Color(0xFF5A2E2E)
                    )
                }

            } else {

                // ===== LIST =====
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 220.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 40.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(history) { item ->
                        HistoryItem(item)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    item: HistoryItemUi
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {

        // ===== FRAME =====
        Image(
            painter = painterResource(
                if (item.isSuccess)
                    R.drawable.success_frame
                else
                    R.drawable.fail_frame
            ),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {

            // ===== TITLE =====
            Text(
                text = if (item.isSuccess)
                    "Session Complete!"
                else
                    "Session Failed!",
                fontSize = 18.sp,
                fontFamily = pixelify,
                color = if (item.isSuccess)
                    Color(0xFF2E8B57)
                else
                    Color(0xFFBD1025)
            )

            Spacer(Modifier.height(4.dp))

            // ===== TIME =====
            Text(
                text = item.durationText,
                fontSize = 32.sp,
                fontFamily = jersey20,
                color = Color(0xFF5A2E2E)
            )

            Spacer(Modifier.weight(1f))

            // ===== BOTTOM =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = item.dateText,
                    fontSize = 14.sp,
                    fontFamily = jersey20,
                    color = Color(0xFF5A2E2E)
                )

                Text(
                    text = item.expText,
                    fontSize = 16.sp,
                    fontFamily = jersey20,
                    color = if (item.isSuccess)
                        Color(0xFF2E8B57)
                    else
                        Color(0xFFBD1025)
                )
            }
        }
    }
}
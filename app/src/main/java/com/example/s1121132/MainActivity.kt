package com.example.s1121132

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.s1121132.ui.theme.S1121132Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            S1121132Theme {
                AppContent(onExitApp = { finish() })
            }
        }
    }
}

@Composable
fun AppContent(onExitApp: () -> Unit) {
    // 定義顏色循環
    val colors = listOf(
        Color(0xff95fe95),
        Color(0xfffdca0f),
        Color(0xfffea4a4),
        Color(0xffa5dfed)
    )
    var colorIndex by remember { mutableStateOf(0) }
    var elapsedTime by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var imagePosition by remember { mutableStateOf(0f) }
    val screenWidth = 1080.dp // 假設螢幕寬度

    // 開始移動圖示
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (imagePosition < screenWidth.value) {
                delay(1000L) // 每秒移動
                elapsedTime++
                imagePosition += 50f // 每次右移 50px
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors[colorIndex])
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    // 根據滑動方向切換顏色
                    if (dragAmount > 0) {
                        // 右滑，顏色向後切換
                        colorIndex = (colorIndex + 1) % colors.size
                    } else if (dragAmount < 0) {
                        // 左滑，顏色向前切換
                        colorIndex = (colorIndex - 1 + colors.size) % colors.size
                    }
                }
            },
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顯示標題
            Text(text = "2024期末上機考(資管二B 顧晉瑋)")
            Spacer(modifier = Modifier.height(16.dp))

            // 加入圖片
            Image(
                painter = painterResource(id = R.drawable.class_b), // 確保圖片名稱和位置正確
                contentDescription = "資管二B圖片",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 遊戲持續時間
            Text(text = "遊戲持續時間：${elapsedTime} 秒")
            Spacer(modifier = Modifier.height(16.dp))

            // 成績顯示
            Text(text = "您的成績：${score} 分")
            Spacer(modifier = Modifier.height(16.dp))

            // 結束App按鈕
            Button(onClick = onExitApp) {
                Text(text = "結束App")
            }
        }

        // 瑪利亞位置圖示
        Image(
            painter = painterResource(id = R.drawable.maria2), // 圖示名稱需對應正確
            contentDescription = "瑪利亞圖示",
            modifier = Modifier
                .size(200.dp)
                .offset(x = imagePosition.dp, y = 600.dp) // 固定底部位置
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppContent() {
    S1121132Theme {
        AppContent(onExitApp = {})
    }
}

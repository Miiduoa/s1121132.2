package com.example.s1121132

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
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
    var imagePositionX by remember { mutableStateOf(0f) }
    val screenWidth = 1080f // 假設螢幕寬度
    val imageSize = 200f // 圖示大小
    val maxImagePositionX = screenWidth - imageSize // 限制圖片最大 X 偏移量

    var mariaImage by remember { mutableStateOf(R.drawable.maria2) }

    // 開始移動圖示
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (imagePositionX < maxImagePositionX) {
                delay(1000L) // 每秒移動
                elapsedTime++
                imagePositionX = (imagePositionX + 50f).coerceAtMost(maxImagePositionX) // 限制最大偏移量
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors[colorIndex])
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // 放開滑鼠或手指後切換顏色
                        colorIndex = (colorIndex + 1) % colors.size
                    },
                    onHorizontalDrag = { _, _ ->
                        // 滑動過程中不切換顏色
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // 顯示標題
            Text(text = "2024期末上機考(資管二B 顧晉瑋)")
            Spacer(modifier = Modifier.height(16.dp))

            // 加入圖片，放大以左右貼齊螢幕
            Image(
                painter = painterResource(id = R.drawable.class_b), // 確保圖片名稱和位置正確
                contentDescription = "資管二B圖片",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp), // 可調整高度
                contentScale = ContentScale.Crop
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
            painter = painterResource(id = mariaImage), // 圖示名稱需對應正確
            contentDescription = "瑪利亞圖示",
            modifier = Modifier
                .size(200.dp)
                .offset(x = imagePositionX.dp, y = 450.dp) // 固定底部位置
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (colors[colorIndex] == Color(0xff95fe95)) {
                                score++ // 如果顏色相同加1分
                            } else {
                                score-- // 如果顏色不同扣1分
                            }
                            // 隨機更換圖片並重置位置
                            mariaImage = listOf(
                                R.drawable.maria0,
                                R.drawable.maria1,
                                R.drawable.maria2,
                                R.drawable.maria3
                            ).random()
                            imagePositionX = 0f
                        }
                    )
                }
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

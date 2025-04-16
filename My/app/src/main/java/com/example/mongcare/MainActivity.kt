package com.example.mongcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mongcare.ui.MainScreen
import com.example.mongcare.ui.theme.MongCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MongCareTheme {
                MainScreen()
            }
        }
    }
}

package com.example.mongcare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mongcare.R.layout.activity_main

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ 이게 있어야 activity_main.xml 화면이 나옴!
        setContentView(activity_main)
    }
}
package com.example.my

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// 수정: 올바른 R 파일 임포트

class MainActivity : AppCompatActivity(), MainView {  // MainView 인터페이스 구현
    private lateinit var etInput: EditText
    private lateinit var btnProcess: Button
    private lateinit var tvResult: TextView
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etInput = findViewById(R.id.etInput)
        btnProcess = findViewById(R.id.btnProcess)
        tvResult = findViewById(R.id.tvResult)  // ⭐ 반드시 findViewById로 초기화 ⭐

        presenter = MainPresenter(this)

        btnProcess.setOnClickListener {
            val inputText = etInput.text.toString()
            presenter.onButtonClick(inputText)
        }
    }

    override fun showResult(result: String) {
        tvResult.text = result
    }
}


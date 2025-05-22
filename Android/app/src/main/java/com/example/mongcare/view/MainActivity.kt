package com.example.mongcare.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mongcare.R
import com.example.mongcare.databinding.FragmentMainBinding
import com.example.mongcare.view.fragments.MainFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_layout, MainFragment.newInstance(5))
            .commit()
    }
}

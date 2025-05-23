package com.example.mongcare.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mongcare.R
import com.example.mongcare.databinding.FragmentMainBinding
import com.example.mongcare.view.fragments.WalkTimeFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn = binding.walkTimeButton
        btn.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.walk_time_fragment, WalkTimeFragment.newInstance(5))
                .addToBackStack(null)
                .commit()
        }
    }

}

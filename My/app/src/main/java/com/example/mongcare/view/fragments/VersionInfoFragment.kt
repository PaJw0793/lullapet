package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mongcare.R
import kotlinx.android.synthetic.main.fragment_version_info.*

class VersionInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_version_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        version_info_back_button.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 여기에 실제 앱 버전/기기 정보 표시 로직 추가 가능
        version_info_content.text = "앱 버전: 1.0.0\n기기 버전: 2.1.3"
    }
}

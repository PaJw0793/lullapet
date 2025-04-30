package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import your.package.name.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼
        binding.settingsBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 기기 연결 상태 확인 버튼 클릭 이벤트
        binding.settingsDeviceStatusButton.setOnClickListener {
            // TODO: 연결 상태 확인 화면으로 이동 or 동작 수행
        }

        // 버전 확인 버튼 클릭 이벤트
        binding.settingsVersionButton.setOnClickListener {
            // TODO: 앱 및 기기 버전 정보 표시
        }

        // 스위치 상태 변경 리스너
        binding.settingsAutoToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            // TODO: 스위치 상태 저장 or 동작 수행
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

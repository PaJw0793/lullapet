package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.FragmentSettingsBinding

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

        binding.settingsBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.settingsDeviceStatusButton.setOnClickListener {
            // TODO: 연결 상태 확인 화면으로 이동
        }

        binding.settingsVersionButton.setOnClickListener {
            // TODO: 버전 정보 화면으로 이동
        }

        binding.settingsAutoToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            // TODO: 상태 저장
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

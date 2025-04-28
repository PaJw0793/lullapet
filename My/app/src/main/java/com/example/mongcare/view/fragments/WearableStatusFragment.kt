package com.example.mongcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.FragmentWearableStatusBinding

class WearableStatusFragment : Fragment() {

    private var _binding: FragmentWearableStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWearableStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 클릭 리스너 예시
        binding.wsBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.wsSettingsButton.setOnClickListener {
            // 설정 화면 이동
        }

        binding.wsDog1Button.setOnClickListener {
            // 강아지1 상태 보기
        }

        binding.wsDog2Button.setOnClickListener {
            // 강아지2 상태 보기
        }

        binding.wsCatButton.setOnClickListener {
            // 고양이 상태 보기
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.FragmentDeviceStatusBinding

class DeviceStatusFragment : Fragment() {
    private var _binding: FragmentDeviceStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceStatusBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼 클릭 리스너 추가
        binding.deviceStatusBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        fun newInstance(param: Int): DeviceStatusFragment {
            val fragment = DeviceStatusFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            return fragment
        }
    }
}
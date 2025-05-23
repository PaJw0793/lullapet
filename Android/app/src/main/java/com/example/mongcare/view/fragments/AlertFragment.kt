package com.example.mongcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.FragmentWalkTimeBinding
import com.example.mongcare.view.fragments.WalkTimeFragment

class AlertFragment : Fragment() {

    private var _binding: FragmentWalkTimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButton.setOnClickListener {
            Toast.makeText(requireContext(), "시간 선택 취소", Toast.LENGTH_SHORT).show()
        }

        binding.okButton.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            Toast.makeText(requireContext(), "$hour 시 $minute 분 선택", Toast.LENGTH_SHORT).show()
        }

        binding.addButton.setOnClickListener {
            Toast.makeText(requireContext(), "산책 시간을 추가합니다", Toast.LENGTH_SHORT).show()
        }

        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val text = if (isChecked) "알림 켜짐" else "알림 꺼짐"
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(param: Int): AlertFragment {
            val fragment = AlertFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            return fragment
        }
    }
}

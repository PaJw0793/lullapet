package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mongcare.R
import com.example.mongcare.databinding.FragmentDeviceStatusBinding
import com.example.mongcare.viewmodel.DeviceStatusViewModel

class DeviceStatusFragment : Fragment() {

    private lateinit var binding: FragmentDeviceStatusBinding
    private val viewModel: DeviceStatusViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_status, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deviceStatusConfirmButton.setOnClickListener {
            Toast.makeText(requireContext(), "확인했습니다!", Toast.LENGTH_SHORT).show()
        }
    }
}

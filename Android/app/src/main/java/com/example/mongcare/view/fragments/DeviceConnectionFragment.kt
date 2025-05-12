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
import com.example.mongcare.databinding.FragmentDeviceConnectionBinding
import com.example.mongcare.databinding.DeviceConnectionItemBinding
import com.example.mongcare.viewmodel.DeviceConnectionViewModel
class DeviceConnectionFragment : Fragment() {
    private lateinit var binding: FragmentDeviceConnectionBinding
    private val viewModel: DeviceConnectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_connection, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = listOf(
            Triple(R.drawable.ic_dog1, "강아지1의\n웨어러블 상태 보기/\n기기 연결 확인") { showToast("강아지1 클릭") },
            Triple(R.drawable.ic_dog2, "강아지2의\n웨어러블 상태 보기/\n기기 연결 확인") { showToast("강아지2 클릭") },
            Triple(R.drawable.ic_cat, "고양이의\n웨어러블 상태 보기/\n기기 연결 확인") { showToast("고양이 클릭") }
        )

        items.forEach { (icon, text, onClick) ->
            val itemBinding = DeviceConnectionItemBinding.inflate(layoutInflater)
            itemBinding.iconRes = icon
            itemBinding.label = text
            itemBinding.deviceItemRoot.setOnClickListener { onClick() }
            binding.deviceItemContainer.addView(itemBinding.root)
        }

        binding.deviceBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
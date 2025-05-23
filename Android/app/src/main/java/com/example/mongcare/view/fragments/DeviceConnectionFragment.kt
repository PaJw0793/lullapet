package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.DeviceConnectionItemBinding
import com.example.mongcare.databinding.FragmentDeviceConnectionBinding
import com.example.mongcare.presenter.deviceconnection.DeviceConnectionContract
import com.example.mongcare.presenter.deviceconnection.DeviceConnectionPresenter

class DeviceConnectionFragment : Fragment(), DeviceConnectionContract.View {

    private lateinit var binding: FragmentDeviceConnectionBinding
    private lateinit var presenter: DeviceConnectionContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceConnectionBinding.inflate(inflater, container, false)
        presenter = DeviceConnectionPresenter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()

        binding.deviceBackButton.setOnClickListener {
            presenter.onBackPressed()
        }
    }

    override fun addDeviceItem(iconRes: Int, label: String, onClick: () -> Unit) {
        val itemBinding = DeviceConnectionItemBinding.inflate(layoutInflater)
        itemBinding.iconRes = iconRes
        itemBinding.label = label
        itemBinding.deviceItemRoot.setOnClickListener { onClick() }
        binding.deviceItemContainer.addView(itemBinding.root)
    }

    override fun goBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    override fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(param: Int): DeviceConnectionFragment {
            val fragment = DeviceConnectionFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            return fragment
        }
    }
}

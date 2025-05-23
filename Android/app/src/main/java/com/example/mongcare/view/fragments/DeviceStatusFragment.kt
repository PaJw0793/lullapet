package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.FragmentDeviceStatusBinding
import com.example.mongcare.presenter.devicestatus.DeviceStatusContract
import com.example.mongcare.presenter.devicestatus.DeviceStatusPresenter

class DeviceStatusFragment : Fragment(), DeviceStatusContract.View {

    private lateinit var binding: FragmentDeviceStatusBinding
    private lateinit var presenter: DeviceStatusContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceStatusBinding.inflate(inflater, container, false)
        presenter = DeviceStatusPresenter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deviceStatusConfirmButton.setOnClickListener {
            presenter.onConfirmClicked()
        }

        binding.deviceStatusBackButton.setOnClickListener {
            presenter.onBackPressed()
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun goBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
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

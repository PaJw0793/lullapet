package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mongcare.Interfaces.PageName
import com.example.mongcare.R
import com.example.mongcare.Interfaces.FragmentChange
import com.example.mongcare.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var fragmentChange : FragmentChange

    var settingsDeviceStatusButton:Button? = null
    var settingsVersionButton:Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        this.settingsDeviceStatusButton = view?.findViewById<Button>(R.id.settings_deviceStatusButton)
        this.settingsVersionButton = view?.findViewById<Button>(R.id.settings_versionButton)

        binding.settingsDeviceStatusButton.setOnClickListener {
            fragmentChange?.setFrag(PageName.DIVICESTATUS.ordinal)
        }
        binding.settingsVersionButton.setOnClickListener {
            fragmentChange?.setFrag(PageName.version.ordinal)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼 클릭 리스너 추가
        binding.deviceBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance(param: Int, Fragmentch: FragmentChange): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            fragment.fragmentChange = Fragmentch
            return fragment
        }
    }
}

package com.example.mongcare.view.fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.mongcare.R
import android.view.ViewGroup
import android.widget.Button
import com.example.mongcare.Interfaces.FragmentChange
import com.example.mongcare.databinding.FragmentMainBinding
import com.example.mongcare.Interfaces.PageName
import com.example.mongcare.util.FirebaseReadHeartRateExample
import com.example.mongcare.util.FirebaseReadMotionExample
import com.example.mongcare.util.FirebaseReadTemperatureExample

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var fragmentChange : FragmentChange
    var walkTimeButton:Button? = null
    var settingButton:Button? = null
    var aiSleepEnvironmentButton:Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        this.walkTimeButton = view?.findViewById<Button>(R.id.walk_time_button)
        this.aiSleepEnvironmentButton = view?.findViewById<Button>(R.id.ai_sleep_environment_button)
        this.settingButton = view?.findViewById<Button>(R.id.settings_Button)
        binding.walkTimeButton.setOnClickListener {
            fragmentChange?.setFrag(PageName.WALKTIME.ordinal)
        }
        binding.aiSleepEnvironmentButton.setOnClickListener {
            fragmentChange?.setFrag(PageName.AIRECOMMEND.ordinal)
        }
        binding.settingsButton.setOnClickListener {
            fragmentChange?.setFrag(PageName.SETTINGS.ordinal)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 심박수 실시간 반영
        FirebaseReadHeartRateExample.addHeartRateListener { heartRate ->
            binding.textHeartRate.text = heartRate?.toString() ?: "-"
        }

        // 체온(여기서는 motion값) 실시간 반영
        FirebaseReadMotionExample.addMotionListener { motion ->
            binding.textSleepTime.text = motion?.toString() ?: "-"
        }

        // 수면시간(여기서는 temperature값) 실시간 반영
        FirebaseReadTemperatureExample.addTemperatureListener { temperature ->
            binding.textTemperature.text = temperature?.toString() ?: "-"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(param: Int, Fragmentch: FragmentChange): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            fragment.fragmentChange = Fragmentch
            return fragment
        }
    }
}

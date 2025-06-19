package com.example.mongcare.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mongcare.Interfaces.FragmentChange
import com.example.mongcare.R
import com.example.mongcare.databinding.FragmentMainBinding
import com.example.mongcare.databinding.FragmentWalkTimeBinding
import com.example.mongcare.databinding.FragmentAiRecommendationBinding
import com.example.mongcare.view.fragments.AIRecommendationFragment
import com.example.mongcare.view.fragments.MainFragment
import com.example.mongcare.view.fragments.WalkTimeFragment
import com.example.mongcare.Interfaces.PageName
import com.example.mongcare.databinding.FragmentDeviceStatusBinding
import com.example.mongcare.databinding.FragmentSettingsBinding
import com.example.mongcare.databinding.FragmentVersionInfoBinding
import com.example.mongcare.databinding.FragmentDeviceConnectionBinding
import com.example.mongcare.view.fragments.DeviceConnectionFragment
import com.example.mongcare.view.fragments.DeviceStatusFragment
import com.example.mongcare.view.fragments.SettingsFragment
import com.example.mongcare.view.fragments.VersionInfoFragment


class MainActivity : AppCompatActivity(), FragmentChange {

    private lateinit var binding_main: FragmentMainBinding
    private lateinit var binding_walk: FragmentWalkTimeBinding
    private lateinit var binding_aiecom: FragmentAiRecommendationBinding
    private lateinit var binding_setting: FragmentSettingsBinding
    private lateinit var binding_deviceConnection: FragmentDeviceConnectionBinding
    private lateinit var binding_deviceStatus: FragmentDeviceStatusBinding
    private lateinit var binding_versionInfo: FragmentVersionInfoBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_main = FragmentMainBinding.inflate(layoutInflater)
        binding_walk = FragmentWalkTimeBinding.inflate(layoutInflater)
        binding_aiecom = FragmentAiRecommendationBinding.inflate(layoutInflater)
        binding_setting = FragmentSettingsBinding.inflate(layoutInflater)
        binding_deviceConnection = FragmentDeviceConnectionBinding.inflate(layoutInflater)
        binding_deviceStatus = FragmentDeviceStatusBinding.inflate(layoutInflater)
        binding_versionInfo = FragmentVersionInfoBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)

        var view1 = binding_main
        var view2 = binding_walk
        var view3 = binding_aiecom
        var view4 = binding_setting
        var view5 = binding_deviceConnection
        var view6 = binding_deviceStatus
        var view7 = binding_versionInfo

        view1.walkTimeButton.setOnClickListener {
            setFrag(PageName.WALKTIME.ordinal)
        }

        setFrag(PageName.MAIN.ordinal)
    }

    override fun setFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()

        //fragnum에 따라 fragment 교체
        when (fragNum) {
            PageName.MAIN.ordinal -> {
                ft.replace(R.id.fragmentFrame, MainFragment.newInstance(5, this))
                    .commit()
            }

            PageName.WALKTIME.ordinal -> {
                ft.replace(R.id.fragmentFrame, WalkTimeFragment.newInstance(5))
                    .commit()
            }

            PageName.AIRECOMMEND.ordinal -> {
                ft.replace(R.id.fragmentFrame, AIRecommendationFragment.newInstance(5))
                    .commit()
            }

            PageName.SETTINGS.ordinal -> {
                ft.replace(R.id.fragmentFrame, SettingsFragment.newInstance(5, this))
                    .commit()
            }

            PageName.DIVICECONNECT.ordinal -> {
                ft.replace(R.id.fragmentFrame, DeviceConnectionFragment.newInstance(5))
                    .commit()
            }

            PageName.DIVICESTATUS.ordinal -> {
                ft.replace(R.id.fragmentFrame, DeviceStatusFragment.newInstance(5))
                    .commit()
            }

            PageName.version.ordinal -> {
                ft.replace(R.id.fragmentFrame, VersionInfoFragment.newInstance(5))
                    .commit()
            }
        }
    }
}
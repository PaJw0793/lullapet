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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.graphics.Color
import java.util.LinkedList

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
            fragmentChange.setFrag(PageName.WALKTIME.ordinal)
        }
        binding.aiSleepEnvironmentButton.setOnClickListener {
            fragmentChange.setFrag(PageName.AIRECOMMEND.ordinal)
        }
        binding.settingsButton.setOnClickListener {
            fragmentChange.setFrag(PageName.SETTINGS.ordinal)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 심박수 그래프 관련 초기화
        val heartRateGraph = view.findViewById<LineChart>(R.id.heartRateGraphPlaceholder)
        val entries = LinkedList<Entry>()
        val maxPoints = 40 // 500ms 간격, 약 20초치 데이터
        val startTime = System.currentTimeMillis()
        val dataSet = LineDataSet(entries, "").apply {
            color = Color.BLUE
            setDrawCircles(false) // 점 없이 선만
            setDrawValues(false)
            lineWidth = 2f
            setDrawFilled(false)
            setDrawHighlightIndicators(false)
        }
        heartRateGraph.data = LineData(dataSet)
        heartRateGraph.description.isEnabled = false
        heartRateGraph.legend.isEnabled = false
        heartRateGraph.axisRight.isEnabled = false
        heartRateGraph.axisLeft.textColor = Color.BLACK
        heartRateGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        heartRateGraph.xAxis.textColor = Color.BLACK
        heartRateGraph.xAxis.setDrawGridLines(false)
        heartRateGraph.axisLeft.setDrawGridLines(false)
        heartRateGraph.setTouchEnabled(false)
        heartRateGraph.setScaleEnabled(false)
        heartRateGraph.setPinchZoom(false)

        // 500ms마다 마지막 심박수 값만 그래프에 추가
        var lastHeartRate: Double? = null
        FirebaseReadHeartRateExample.addHeartRateListener { heartRate ->
            lastHeartRate = heartRate?.toDouble()
            binding.textHeartRate.text = heartRate?.toString() ?: "-"
        }
        view.post(object : Runnable {
            override fun run() {
                val now = System.currentTimeMillis()
                val x = (now - startTime) / 500f
                lastHeartRate?.let { hr ->
                    entries.add(Entry(x, hr.toFloat()))
                    if (entries.size > maxPoints) entries.removeFirst()
                    dataSet.notifyDataSetChanged()
                    heartRateGraph.data.notifyDataChanged()
                    heartRateGraph.notifyDataSetChanged()
                    heartRateGraph.invalidate()
                }
                view.postDelayed(this, 500)
            }
        })

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


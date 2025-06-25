package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.FragmentWalkTimeBinding
import com.example.mongcare.presenter.walktime.WalkTimeContract
import com.example.mongcare.R

class WalkTimeFragment : Fragment() {
    private var _binding: FragmentWalkTimeBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: WalkTimeContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkTimeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼 클릭 리스너 추가
        binding.deviceBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.addButton.setOnClickListener {
            val hour: Int
            val minute: Int

            // API 레벨에 따라 TimePicker 시간 가져오는 방식 분기
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hour = binding.timePicker.hour
                minute = binding.timePicker.minute
            } else {
                @Suppress("DEPRECATION")
                hour = binding.timePicker.currentHour
                @Suppress("DEPRECATION")
                minute = binding.timePicker.currentMinute
            }
            // Presenter에게 시간 추가 요청
            presenter.onAddButtonClicked(hour, minute)
        }
    }

    override fun addTimeEntryToView(formattedTime: String) {
        // View의 역할: UI 업데이트 (새로운 시간 항목 뷰 생성 및 추가)
        val newTimeEntryView = LayoutInflater.from(context).inflate(R.layout.item_todo, getWalkTimeListContainer(), false)

        val todoTitleTextView = newTimeEntryView.findViewById<TextView>(R.id.todoTitle)
        todoTitleTextView.text = formattedTime

        val deleteButton = newTimeEntryView.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            // 삭제 버튼 클릭 시, 해당 뷰를 직접 제거 (간단한 UI 조작이므로 View에서 직접 처리)
            removeTimeEntryFromView(newTimeEntryView)
        }

        // walkTimeListContainer의 마지막 자식(추가하기 버튼) 앞에 새 항목 추가
        getWalkTimeListContainer().addView(newTimeEntryView, getWalkTimeListContainer().childCount - 1)
    }

    override fun removeTimeEntryFromView(viewToRemove: View) {
        // View의 역할: UI에서 특정 뷰 제거
        getWalkTimeListContainer().removeView(viewToRemove)
    }

    override fun getWalkTimeListContainer(): ViewGroup {
        // View의 역할: Presenter에게 UI 컨테이너 제공
        return binding.walkTimeListContainer
    }


    companion object {
        fun newInstance(param: Int): WalkTimeFragment {
            val fragment = WalkTimeFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            return fragment
        }
    }
}
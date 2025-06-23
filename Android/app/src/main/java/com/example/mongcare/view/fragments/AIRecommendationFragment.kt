package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mongcare.databinding.FragmentAiRecommendationBinding
import com.example.mongcare.presenter.recommendation.AIRecommendationContract
import com.example.mongcare.view.adapter.ChatAdapter
import com.example.mongcare.view.model.ChatMessage

class AIRecommendationFragment : Fragment() {
    private var _binding: FragmentAiRecommendationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiRecommendationBinding.inflate(layoutInflater, container, false)
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
    }

    companion object {
        fun newInstance(param: Int): AIRecommendationFragment {
            val fragment = AIRecommendationFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            return fragment
        }
    }
}

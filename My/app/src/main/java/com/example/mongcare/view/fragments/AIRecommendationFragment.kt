package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mongcare.databinding.FragmentAiRecommendationBinding

class AIRecommendationFragment : Fragment() {

    private var _binding: FragmentAiRecommendationBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter()
        binding.aiChatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.aiChatRecyclerView.adapter = chatAdapter

        binding.aiSendButton.setOnClickListener {
            val message = binding.aiMessageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                chatAdapter.addMessage(ChatMessage(sender = "주인", content = message))
                chatAdapter.addMessage(ChatMessage(sender = "AI", content = "숙면 환경을 위해 조도는 20%, 온도는 24도를 권장합니다."))
                binding.aiMessageEditText.text.clear()
            } else {
                Toast.makeText(requireContext(), "메시지를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

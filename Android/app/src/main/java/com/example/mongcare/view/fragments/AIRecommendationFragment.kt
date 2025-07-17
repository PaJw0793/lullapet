package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mongcare.databinding.FragmentAiRecommendationBinding
import com.example.mongcare.presenter.recommendation.AIRecommendationContract
import com.example.mongcare.presenter.recommendation.AIRecommendationPresenter
import com.example.mongcare.view.adapter.ChatAdapter
import com.example.mongcare.view.model.ChatMessage

class AIRecommendationFragment : Fragment(), AIRecommendationContract.View {
    private var _binding: FragmentAiRecommendationBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: AIRecommendationContract.Presenter
    private lateinit var chatAdapter: ChatAdapter

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

        // ChatAdapter 및 RecyclerView 설정
        chatAdapter = ChatAdapter()
        binding.aiChatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        // 테스트용 메시지 추가 (정상 표시 확인용)
        chatAdapter.addMessage(ChatMessage("AI", "안녕하세요! 어떤 도움이 필요하신가요?"))

        // 반드시 실제 API 키로 교체하세요!
        val mistralApiKey = "69AClj2z5G8TawQvXDrSH9lpCsQNmIAt"
        presenter = AIRecommendationPresenter(this, mistralApiKey)

        // 엔터키 입력 시 메시지 전송 처리
        binding.aiMessageEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_NULL) {
                val message = binding.aiMessageEditText.text.toString().trim()
                if (message.isNotEmpty()) {
                    presenter.onSendClicked(message)
                    binding.aiMessageEditText.text?.clear()
                }
                true
            } else {
                false
            }
        }

        // 보내기 버튼 클릭 시 메시지 전송
        binding.aiSendButton.setOnClickListener {
            val message = binding.aiMessageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                presenter.onSendClicked(message)
                binding.aiMessageEditText.text?.clear()
            }
        }
    }

    override fun addUserMessage(message: String) {
        chatAdapter.addMessage(ChatMessage("User", message))
        binding.aiChatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun addAIMessage(message: String) {
        chatAdapter.addMessage(ChatMessage("AI", message))
        binding.aiChatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun clearInput() {
        binding.aiMessageEditText.text?.clear()
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

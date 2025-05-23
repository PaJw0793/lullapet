package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mongcare.databinding.FragmentAiRecommendationBinding
import com.example.mongcare.presenter.recommendation.AIRecommendationContract
import com.example.mongcare.presenter.recommendation.AIRecommendationPresenter
import com.example.mongcare.view.adapter.ChatAdapter
import com.example.mongcare.view.model.ChatMessage

class AIRecommendationFragment : Fragment(), AIRecommendationContract.View {

    private lateinit var binding: FragmentAiRecommendationBinding
    private lateinit var presenter: AIRecommendationContract.Presenter
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAiRecommendationBinding.inflate(inflater, container, false)
        presenter = AIRecommendationPresenter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter()
        binding.aiChatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.aiChatRecyclerView.adapter = chatAdapter

        binding.aiSendButton.setOnClickListener {
            val message = binding.aiMessageEditText.text.toString()
            presenter.onSendClicked(message)
        }
    }

    override fun addUserMessage(message: String) {
        chatAdapter.addMessage(ChatMessage(sender = "주인", content = message))
    }

    override fun addAIMessage(message: String) {
        chatAdapter.addMessage(ChatMessage(sender = "AI", content = message))
    }

    override fun clearInput() {
        binding.aiMessageEditText.text.clear()
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

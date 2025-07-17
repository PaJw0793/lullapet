package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mongcare.R
import com.example.mongcare.databinding.FragmentWalkTimeBinding
import com.example.mongcare.presenter.walktime.WalkTimeContract
import com.example.mongcare.presenter.walktime.WalkTimePresenter

class WalkTimeFragment : Fragment(), WalkTimeContract.View {

    private var _binding: FragmentWalkTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: WalkTimeContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = WalkTimePresenter(this)

        binding.deviceBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.addButton.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            presenter.onAddButtonClicked(hour, minute)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun addTimeEntryToView(formattedTime: String) {
        val newTimeEntryView = LayoutInflater.from(context).inflate(R.layout.item_todo, getWalkTimeListContainer(), false)

        val todoTitleTextView = newTimeEntryView.findViewById<TextView>(R.id.todoTitle)
        todoTitleTextView.text = formattedTime

        val deleteButton = newTimeEntryView.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            removeTimeEntryFromView(newTimeEntryView)
        }

        getWalkTimeListContainer().addView(newTimeEntryView, getWalkTimeListContainer().childCount - 1)
    }

    override fun removeTimeEntryFromView(viewToRemove: View) {
        getWalkTimeListContainer().removeView(viewToRemove)
    }

    override fun getWalkTimeListContainer(): ViewGroup {
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
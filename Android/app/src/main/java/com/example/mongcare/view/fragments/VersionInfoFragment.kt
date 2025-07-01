package com.example.mongcare.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mongcare.databinding.FragmentVersionInfoBinding
import com.example.mongcare.R

class VersionInfoFragment : Fragment() {

    private var _binding: FragmentVersionInfoBinding? = null
    private val binding get() = _binding!!

    var VersionInfotext: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVersionInfoBinding.inflate(inflater, container, false)
        this.VersionInfotext = view?.findViewById<TextView>(R.id.version_info_content)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼 클릭 리스너 추가
        binding.deviceBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.versionInfoContent.text = R.string.desc_version_info.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(param: Int): VersionInfoFragment {
            val fragment = VersionInfoFragment()
            val args = Bundle()
            args.putInt("param", param)
            fragment.arguments = args
            return fragment
        }
    }
}

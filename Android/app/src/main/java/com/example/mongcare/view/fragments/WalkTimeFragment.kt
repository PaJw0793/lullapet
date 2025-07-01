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

/**
 * 산책 시간 관리 화면을 담당하는 Fragment 입니다.
 * WalkTimeContract.View 인터페이스를 구현하여 Presenter가 UI를 제어할 수 있도록 합니다.
 * 이 클래스는 'View'의 역할을 수행하며, UI 렌더링과 사용자 입력 처리에만 집중합니다.
 */
class WalkTimeFragment : Fragment(), WalkTimeContract.View {
    // 뷰 바인딩 객체입니다. null을 허용하는 타입(_binding)과 허용하지 않는 타입(binding) 두 개를 사용합니다.
    // 이는 onViewCreated와 onDestroyView 사이에서만 binding 객체를 안전하게 사용하기 위함입니다.
    private var _binding: FragmentWalkTimeBinding? = null
    private val binding get() = _binding!! // _binding이 null이 아님을 확신하고 사용할 때 쓰는 프로퍼티

    // 이 View를 제어할 Presenter 인스턴스입니다.
    // Contract 인터페이스 타입을 사용하여 Presenter의 구체적인 구현을 몰라도 상호작용할 수 있습니다.
    private lateinit var presenter: WalkTimeContract.Presenter

    /**
     * Fragment의 UI를 생성하는 단계입니다. (생명주기 1단계)
     * XML 레이아웃을 실제 View 객체로 만드는(inflate) 역할을 합니다.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 뷰 바인딩을 초기화하고, 생성된 뷰의 최상위 루트(root)를 반환합니다.
        _binding = FragmentWalkTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * onCreateView를 통해 View 생성이 완료된 직후 호출됩니다. (생명주기 2단계)
     * View에 대한 초기화(리스너 설정, 데이터 바인딩 등)를 수행하기에 가장 적합한 곳입니다.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Presenter를 초기화합니다.
        // 'this'는 WalkTimeContract.View를 구현한 WalkTimeFragment 자신을 의미합니다.
        // 이제 Presenter는 이 Fragment의 UI를 제어할 수 있습니다.
        presenter = WalkTimePresenter(this)

        // 뒤로가기 버튼(deviceBackButton)을 누르면, 현재 Activity의 뒤로가기 동작을 수행합니다.
        binding.deviceBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // '추가하기' 버튼(addButton)을 누르면 실행될 로직입니다.
        binding.addButton.setOnClickListener {
            // TimePicker에서 현재 선택된 시간과 분을 가져옵니다.
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute

            // 시간 포맷팅과 같은 '비즈니스 로직'은 직접 처리하지 않습니다.
            // 대신, Presenter에게 "사용자가 추가 버튼을 눌렀다"는 사실과 데이터를 전달합니다.
            presenter.onAddButtonClicked(hour, minute)
        }
    }

    /**
     * Fragment의 View가 파괴될 때 호출됩니다. (생명주기 3단계)
     * View와 관련된 리소스를 정리하여 메모리 누수를 방지해야 합니다.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // _binding 참조를 null로 만들어 메모리에서 해제될 수 있도록 돕습니다.
        // 이렇게 하지 않으면 Fragment는 계속 View를 참조하게 되어 메모리 누수가 발생할 수 있습니다.
        _binding = null
    }

    // --- WalkTimeContract.View 인터페이스 구현부 --- //
    // 아래 메서드들은 Presenter의 요청에 따라 UI를 업데이트하는 역할만 수행합니다.

    /**
     * Presenter로부터 받은 포맷팅된 시간 문자열을 화면에 추가합니다.
     * @param formattedTime Presenter가 가공해서 전달해준 시간 문자열 (예: "오후 02:30")
     */
    override fun addTimeEntryToView(formattedTime: String) {
        // item_todo.xml 레이아웃을 기반으로 새로운 시간 항목 View를 동적으로 생성합니다.
        val newTimeEntryView = LayoutInflater.from(context).inflate(R.layout.item_todo, getWalkTimeListContainer(), false)

        // 생성된 View 안의 TextView를 찾아 Presenter가 전달한 텍스트로 설정합니다.
        val todoTitleTextView = newTimeEntryView.findViewById<TextView>(R.id.todoTitle)
        todoTitleTextView.text = formattedTime

        // 생성된 View 안의 삭제 버튼에 대한 클릭 리스너를 설정합니다.
        val deleteButton = newTimeEntryView.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            // 사용자가 삭제 버튼을 누르면, 해당 항목 View를 화면에서 제거합니다.
            // 이 로직은 간단한 UI 조작이므로 Presenter를 거치지 않고 View에서 직접 처리할 수 있습니다.
            removeTimeEntryFromView(newTimeEntryView)
        }

        // 생성된 시간 항목 View를 '추가하기' 버튼 바로 위에 추가합니다.
        getWalkTimeListContainer().addView(newTimeEntryView, getWalkTimeListContainer().childCount - 1)
    }

    /**
     * 특정 시간 항목 View를 화면에서 제거합니다.
     * @param viewToRemove 화면에서 제거할 View 객체
     */
    override fun removeTimeEntryFromView(viewToRemove: View) {
        getWalkTimeListContainer().removeView(viewToRemove)
    }

    /**
     * 시간 항목들이 추가될 부모 컨테이너(ViewGroup)를 Presenter에게 제공합니다.
     * (이 예제에서는 직접 사용되지 않았지만, Presenter가 컨테이너에 직접 접근해야 할 경우를 위한 메서드)
     * @return 시간 항목들이 들어갈 LinearLayout
     */
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
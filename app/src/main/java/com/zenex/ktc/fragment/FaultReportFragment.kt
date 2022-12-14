package com.zenex.ktc.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.zenex.ktc.R
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.adapter.ListReportAdapter
import com.zenex.ktc.api.param.response.ParamGetFaultReportListResponse
import com.zenex.ktc.data.DummyData
import com.zenex.ktc.data.UserData
import com.zenex.ktc.databinding.FragmentFaultReportBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class FaultReportFragment : Fragment() {
    private var _binding: FragmentFaultReportBinding? = null
    private val binding get() = _binding!!

    var userData: UserData? = null

    var currentScreen = "New"

    private val dummyData = DummyData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaultReportBinding.inflate(layoutInflater, container, false)

        val activity = activity as BaseActivity
        userData = activity.userData

        binding.clMain.setOnClickListener {
            val act = activity as BaseActivity
            act.hideSoftKeyboard()
            act.currentFocus?.clearFocus()
        }

//        dummyData.addTestItem()
//        val testItem = dummyData.testItem
//        setRecyclerView(testItem, "New")
//        setButtonToggle(testItem)

        loadFaultReport(true, currentScreen)

        dateOnTouch(binding.tilDateFrom)
        dateOnTouch(binding.tilDateTo)

        binding.btnSearch.setOnClickListener {
            searchFaultReport()
        }

        return binding.root
    }

    private fun searchFaultReport() {
        loadFaultReport(false, currentScreen)
    }


    private fun loadFaultReport(firstLoad: Boolean, currentScreen: String){
        for (status in listOf("New", "In Progress", "Completed")){
            userData?.getFaultReportList(
                requireContext(), this, status,
                binding.tilAssetId.editText?.text.toString(),
                binding.tilSiteCode.editText?.text.toString(),
                binding.tilDateFrom.editText?.text.toString(),
                binding.tilDateTo.editText?.text.toString(),
                firstLoad, currentScreen
            )
        }
    }

    fun setButtonToggleNew(item: ArrayList<ParamGetFaultReportListResponse.Data>?){
        binding.btnNew.setOnClickListener {
            currentScreen = "New"
            setRecyclerView(item, "New")
        }
    }

    fun setButtonToggleInProgress(item: ArrayList<ParamGetFaultReportListResponse.Data>?){
        binding.btnInProgress.setOnClickListener {
            currentScreen = "In Progress"
            setRecyclerView(item, "In Progress")
        }
    }

    fun setButtonToggleCompleted(item: ArrayList<ParamGetFaultReportListResponse.Data>?){
        binding.btnCompleted.setOnClickListener {
            currentScreen = "Completed"
            setRecyclerView(item, "Completed")
        }
    }

    fun setFirstLoad(item: ArrayList<ParamGetFaultReportListResponse.Data>?){
        setRecyclerView(item, "New")
    }


    fun setRecyclerView(item: ArrayList<ParamGetFaultReportListResponse.Data>?, type: String){
        binding.rv.adapter = null
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListReportAdapter(requireContext(), item, type, "FR", this)
        adapter.setHasStableIds(true)
        binding.rv.adapter = adapter
    }

    @SuppressLint("SetTextI18n", "NewApi")
    private fun showDatePicker(view: TextInputLayout){
        val datePicker = DatePickerDialog(requireContext(), R.style.Theme_Dialog)
        datePicker.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val date = LocalDate.of(year, monthOfYear+1, dayOfMonth)
            view.editText?.setText(date.toString())
        }
        datePicker.show()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun dateOnTouch(view: TextInputLayout) {
        view.editText?.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP){
                view.requestFocus()
                showDatePicker(view)
                true
            } else false
        }
    }


}
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
import com.zenex.ktc.data.DummyData
import com.zenex.ktc.databinding.FragmentFaultReportBinding
import java.time.LocalDate

class FaultReportFragment : Fragment() {
    private var _binding: FragmentFaultReportBinding? = null
    private val binding get() = _binding!!

    private val dummyData = DummyData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaultReportBinding.inflate(layoutInflater, container, false)

        binding.clMain.setOnClickListener {
            val act = activity as BaseActivity
            act.hideSoftKeyboard()
            act.currentFocus?.clearFocus()
        }

        dummyData.addTestItem()
        val testItem = dummyData.testItem
        setRecyclerView(testItem, "New")
        setButtonToggle(testItem)

        dateOnTouch(binding.tilDateFrom)
        dateOnTouch(binding.tilDateTo)

        return binding.root
    }

    private fun setButtonToggle(item: ArrayList<String>){
        binding.btnNew.setOnClickListener { setRecyclerView(item, "New") }
        binding.btnInProgress.setOnClickListener { setRecyclerView(item, "In Progress") }
        binding.btnCompleted.setOnClickListener { setRecyclerView(item, "Completed") }
    }

    private fun setRecyclerView(item: ArrayList<String>, type: String){
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
                showDatePicker(view)
                true
            } else false
        }
    }

}
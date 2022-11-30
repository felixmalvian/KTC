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
import com.zenex.ktc.adapter.ListReportAdapter
import com.zenex.ktc.databinding.FragmentEjoBinding
import java.time.LocalDate
import kotlin.collections.ArrayList

class EjoFragment : Fragment() {
    private var _binding: FragmentEjoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEjoBinding.inflate(layoutInflater, container, false)

        val testItem = ArrayList<String>()
        testItem.add("ART123")
        testItem.add("BR457")
        testItem.add("EX456")
        testItem.add("AC546")
        testItem.add("RD64")
        testItem.add("BR9")

        setRecyclerView(testItem, "In Progress")
        setButtonToggle(testItem)

        dateOnTouch(binding.tilDateFrom)
        dateOnTouch(binding.tilDateTo)

        return binding.root
    }

    private fun setButtonToggle(item: ArrayList<String>){
        binding.btnInProgress.setOnClickListener { setRecyclerView(item, "In Progress") }
        binding.btnCompleted.setOnClickListener { setRecyclerView(item, "Completed") }
    }

    private fun setRecyclerView(item: ArrayList<String>, type: String){
        binding.rv.adapter = null
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter =ListReportAdapter(requireContext(), item, type, "EJO", this)
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
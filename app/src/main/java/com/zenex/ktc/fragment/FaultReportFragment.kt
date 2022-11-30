package com.zenex.ktc.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.adapter.ListReportAdapter
import com.zenex.ktc.databinding.FragmentFaultReportBinding

class FaultReportFragment : Fragment() {
    private var _binding: FragmentFaultReportBinding? = null
    private val binding get() = _binding!!

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

        val testItem = ArrayList<String>()
        testItem.add("ART123")
        testItem.add("BR457")
        testItem.add("EX456")
        testItem.add("AC546")
        testItem.add("RD64")
        testItem.add("BR9")

        setRecyclerView(testItem, "New")
        setButtonToggle(testItem)

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

}
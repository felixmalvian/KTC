package com.zenex.ktc.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.data.UserData
import com.zenex.ktc.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setButtonDirection()

        val activity = activity as BaseActivity
        userData = activity.userData

        binding.tvLoginName.text = userData?.AC_LoginName
        setButtonVisibility()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setButtonVisibility(){
        val role = userData?.UR_UserRole
        val listSeeFaultReport = listOf("SITE_USER", "SITE_APPROVER")
        if (role in listSeeFaultReport) {
            binding.cardBtnCreateFaultReport.visibility = VISIBLE
            binding.cardBtnFaultReportListing.visibility = VISIBLE
            binding.cardBtnCreateEjo.visibility = GONE
            binding.cardBtnEjoListing.visibility = GONE
        }

        if (role == "PROCESSOR"){
            binding.cardBtnCreateFaultReport.visibility = VISIBLE
            binding.cardBtnFaultReportListing.visibility = VISIBLE
            binding.cardBtnCreateEjo.visibility = VISIBLE
            binding.cardBtnEjoListing.visibility = VISIBLE
        }
    }

    private fun setButtonDirection(){
        binding.cardBtnCreateFaultReport.setOnClickListener{
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateFaultReportFragment()
            this.findNavController().navigate(direction)
        }

        binding.cardBtnFaultReportListing.setOnClickListener{
            val direction = HomeFragmentDirections.actionHomeFragmentToFaultReportFragment()
            this.findNavController().navigate(direction)
        }

        binding.cardBtnCreateEjo.setOnClickListener{
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateEjoFragment()
            this.findNavController().navigate(direction)
        }

        binding.cardBtnEjoListing.setOnClickListener{
            val direction = HomeFragmentDirections.actionHomeFragmentToEjoFragment()
            this.findNavController().navigate(direction)
        }
    }

}
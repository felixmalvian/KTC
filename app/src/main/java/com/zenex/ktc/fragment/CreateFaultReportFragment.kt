package com.zenex.ktc.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.zenex.ktc.R
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.databinding.FragmentCreateFaultReportBinding

class CreateFaultReportFragment : Fragment() {
    private var _binding: FragmentCreateFaultReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateFaultReportBinding.inflate(layoutInflater, container, false)

        binding.clMain.setOnClickListener {
            val act = activity as BaseActivity
            act.hideSoftKeyboard()
            act.currentFocus?.clearFocus()
        }

        binding.btnSubmit.setOnClickListener {
            Toast.makeText(requireContext(), "Fault Report Submitted!", Toast.LENGTH_SHORT).show()
            val direction = CreateFaultReportFragmentDirections.actionCreateFaultReportFragmentToHomeFragment()
            this.findNavController().navigate(direction)
        }
        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Fault Report Saved!", Toast.LENGTH_SHORT).show()
            val direction = CreateFaultReportFragmentDirections.actionCreateFaultReportFragmentToHomeFragment()
            this.findNavController().navigate(direction)
        }

        setBreakdownItem()

        return binding.root
    }


    @SuppressLint("ResourceAsColor")
    private fun setBreakdownItem(){
        var cabinGlass = false
        binding.btnCabinGlass.setOnClickListener {
            if (!cabinGlass){
                binding.btnCabinGlass.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnCabinGlass.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            cabinGlass = !cabinGlass
        }

        var bucket = false
        binding.btnBucket.setOnClickListener {
            if (!bucket){
                binding.btnBucket.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnBucket.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            bucket = !bucket
        }

        var engine = false
        binding.btnEngine.setOnClickListener {
            if (!engine){
                binding.btnEngine.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnEngine.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            engine = !engine
        }

        var hydraulicHose = false
        binding.btnHydraulicHose.setOnClickListener {
            if (!hydraulicHose){
                binding.btnHydraulicHose.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnHydraulicHose.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            hydraulicHose = !hydraulicHose
        }

        var undercarriage = false
        binding.btnUndercarriage.setOnClickListener {
            if (!undercarriage){
                binding.btnUndercarriage.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnUndercarriage.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            undercarriage = !undercarriage
        }

        var mainPump = false
        binding.btnMainPump.setOnClickListener {
            if (!mainPump){
                binding.btnMainPump.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnMainPump.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            mainPump = !mainPump
        }

        var boomArm = false
        binding.btnBoomArm.setOnClickListener {
            if (!boomArm){
                binding.btnBoomArm.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnBoomArm.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            boomArm = !boomArm
        }

        var attachment = false
        binding.btnAttachment.setOnClickListener {
            if (!attachment){
                binding.btnAttachment.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnAttachment.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            attachment = !attachment
        }

        var others = false
        binding.btnOthers.setOnClickListener {
            if (!others){
                binding.btnOthers.setBackgroundColor(resources.getColor(R.color.blue_ticked))
            } else {
                binding.btnOthers.setBackgroundColor(resources.getColor(R.color.grey_field))
            }
            others = !others
        }
    }

}
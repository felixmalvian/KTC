package com.zenex.ktc.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.zenex.ktc.R
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.data.DummyData
import com.zenex.ktc.data.UserData
import com.zenex.ktc.databinding.FragmentCreateFaultReportBinding

class CreateFaultReportFragment : Fragment() {
    private var _binding: FragmentCreateFaultReportBinding? = null
    private val binding get() = _binding!!
    var userData: UserData? = null

    private val dummyData = DummyData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateFaultReportBinding.inflate(layoutInflater, container, false)

        val activity = activity as BaseActivity
        userData = activity.userData
        setInitialData()
        setYesNoDropdown()
        setDummyData()

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


    private fun setInitialData() {
        binding.tilReportedBy.editText?.setText(userData?.AC_LoginName)

    }
    
    private fun setDummyData() {
        dummyData.addTestSite()
        val testSite = dummyData.testSite
        setDropdownList(binding.tilSiteCode.editText, testSite)

        dummyData.addTestItem()
        val testItem = dummyData.testItem
        setDropdownList(binding.tilAssetId.editText, testItem)

    }

    private fun setYesNoDropdown() {
        dummyData.addYesNo()
        val yesNo = dummyData.yesNo
        setDropdownList(binding.tilWorkingCondition.editText, yesNo)
        setDropdownList(binding.tilAccident.editText, yesNo)
    }

    @SuppressLint("ResourceAsColor")
    private fun setBreakdownItem(){
//        var cabinGlass = false
//        binding.btnCabinGlass.setOnClickListener {
//            if (!cabinGlass){
//                binding.btnCabinGlass.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnCabinGlass.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            cabinGlass = !cabinGlass
//        }
//
//        var bucket = false
//        binding.btnBucket.setOnClickListener {
//            if (!bucket){
//                binding.btnBucket.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnBucket.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            bucket = !bucket
//        }
//
//        var engine = false
//        binding.btnEngine.setOnClickListener {
//            if (!engine){
//                binding.btnEngine.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnEngine.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            engine = !engine
//        }
//
//        var hydraulicHose = false
//        binding.btnHydraulicHose.setOnClickListener {
//            if (!hydraulicHose){
//                binding.btnHydraulicHose.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnHydraulicHose.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            hydraulicHose = !hydraulicHose
//        }
//
//        var undercarriage = false
//        binding.btnUndercarriage.setOnClickListener {
//            if (!undercarriage){
//                binding.btnUndercarriage.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnUndercarriage.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            undercarriage = !undercarriage
//        }
//
//        var mainPump = false
//        binding.btnMainPump.setOnClickListener {
//            if (!mainPump){
//                binding.btnMainPump.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnMainPump.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            mainPump = !mainPump
//        }
//
//        var boomArm = false
//        binding.btnBoomArm.setOnClickListener {
//            if (!boomArm){
//                binding.btnBoomArm.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnBoomArm.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            boomArm = !boomArm
//        }
//
//        var attachment = false
//        binding.btnAttachment.setOnClickListener {
//            if (!attachment){
//                binding.btnAttachment.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnAttachment.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            attachment = !attachment
//        }
//
//        var others = false
//        binding.btnOthers.setOnClickListener {
//            if (!others){
//                binding.btnOthers.setBackgroundColor(resources.getColor(R.color.blue_ticked))
//            } else {
//                binding.btnOthers.setBackgroundColor(resources.getColor(R.color.grey_field))
//            }
//            others = !others
//        }
        
//        binding.ComposeView.setContent {
//            BreakdownCheckbox(breakdownItem = "Attachment")
//        }
    }

    private fun setDropdownList(view: EditText?, data: ArrayList<String>){
        val actView = (view as? AutoCompleteTextView)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, data)
        actView?.setAdapter(arrayAdapter)
    }

}

@Composable
fun BreakdownCheckbox(breakdownItem: String){
    Row(modifier = Modifier.padding(8.dp)) {
        val isChecked = remember { mutableStateOf(false) }

        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it },
            enabled = true,
        )
        Text(text = breakdownItem)
    }
}
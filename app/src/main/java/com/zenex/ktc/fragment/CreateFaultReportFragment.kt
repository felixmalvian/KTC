package com.zenex.ktc.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.zenex.ktc.R
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.data.DummyData
import com.zenex.ktc.data.UserData
import com.zenex.ktc.databinding.FragmentCreateFaultReportBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CreateFaultReportFragment : Fragment() {
    private var _binding: FragmentCreateFaultReportBinding? = null
    private val binding get() = _binding!!
    var userData: UserData? = null

    var breakdownItemChecked = ArrayList<String>()

    private val dummyData = DummyData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateFaultReportBinding.inflate(layoutInflater, container, false)

        val activity = activity as BaseActivity
        userData = activity.userData
        setInitialData()
        setYesNoDropdown()

        setSiteList()

        binding.clMain.setOnClickListener {
            val act = activity as BaseActivity
            act.hideSoftKeyboard()
            act.currentFocus?.clearFocus()
        }

        binding.btnSubmit.setOnClickListener {
//            Toast.makeText(requireContext(), "$breakdownItemChecked", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(), "Fault Report Submitted!", Toast.LENGTH_SHORT).show()

            submitFaultReport()
        }
        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Fault Report Saved!", Toast.LENGTH_SHORT).show()
            val direction = CreateFaultReportFragmentDirections.actionCreateFaultReportFragmentToHomeFragment()
            this.findNavController().navigate(direction)
//            addCheckboxBreakdown("TESTING")
        }

        setBreakdownItem()

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setInitialData() {
        binding.tilReportedBy.editText?.setText(userData?.AC_LoginName)

        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm a")
        val formatted = current.format(formatter)
        binding.tilDateAndTime.editText?.setText(formatted)
    }


    private fun setSiteList(){
        setDropdownList(binding.tilSiteCode.editText, userData?.siteList)
        setOnChangeText()
    }

    private fun setYesNoDropdown() {
        dummyData.addYesNo()
        val yesNo = dummyData.yesNo
        setDropdownList(binding.tilWorkingCondition.editText, yesNo)
        setDropdownList(binding.tilAccident.editText, yesNo)
    }

    private fun setOnChangeText(){
        binding.tilSiteCode.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                loadAssetId(s.toString())
            }

        })

        binding.tilAssetId.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                loadBreakdownItem(s.toString())
            }

        })
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

    private fun setDropdownList(view: EditText?, data: ArrayList<String>?){
        if (data != null){
            val actView = (view as? AutoCompleteTextView)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, data)
            actView?.setAdapter(arrayAdapter)
        }
    }

    private fun addCheckboxBreakdown(breakdownItem: String){
        val checkboxLayout = layoutInflater.inflate(R.layout.breakdown_item_checkbox, null)
        val checkbox: CheckBox = checkboxLayout.findViewById(R.id.cbBreakdownItem)
        checkbox.text = breakdownItem

        checkbox.setOnClickListener{
            if (checkbox.isChecked){
                breakdownItemChecked.add(breakdownItem)
            } else {
                breakdownItemChecked.remove(breakdownItem)
            }
        }
        binding.llBreakdownItem.addView(checkboxLayout)
    }

    fun addCheckboxBreakdown(breakdownItemList: ArrayList<String?>){
        binding.llBreakdownItem.removeAllViews()
        for (item in breakdownItemList){
            if (item != null) {
                addCheckboxBreakdown(item)
            }
        }
    }

    private fun loadAssetId(site: String){
        userData?.getAssetList(requireContext(), site, binding.tilAssetId.editText)
    }

    private fun loadBreakdownItem(assetID: String){
        userData?.getBreakdownItemList(requireContext(), assetID, this)
    }

    private fun submitFaultReport(){
        val issueStatus = checkNull(binding.tilBreakdownDescription.editText)
        val hourmeterStatus = checkNull(binding.tilHourmeter.editText)
        val contactNoStatus = checkNull(binding.tilContactNo.editText)
        val siteCodeStatus = checkNull(binding.tilSiteCode.editText)
        val assetIdStatus = checkNull(binding.tilAssetId.editText)
        val workingConditionStatus = checkNull(binding.tilWorkingCondition.editText)
        val accidentStatus = checkNull(binding.tilAccident.editText)

//        val direction = CreateFaultReportFragmentDirections.actionCreateFaultReportFragmentToHomeFragment()
//        this.findNavController().navigate(direction)
    }

    private fun checkNull(editText: EditText?): Boolean {
        return if (editText?.text.isNullOrBlank()){
            editText?.error = "Cannot be blank"
            false
        } else { true }
    }

}

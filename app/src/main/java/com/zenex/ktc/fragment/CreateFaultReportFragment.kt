package com.zenex.ktc.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.zenex.ktc.R
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.api.param.input.ParamCreateFaultReport
import com.zenex.ktc.data.DummyData
import com.zenex.ktc.data.UserData
import com.zenex.ktc.databinding.FragmentCreateFaultReportBinding
import java.io.FileDescriptor
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CreateFaultReportFragment : Fragment() {
    private var _binding: FragmentCreateFaultReportBinding? = null
    private val binding get() = _binding!!
    var userData: UserData? = null

    var breakdownItemChecked = ArrayList<String>()
    var reportStatus = ""

    private val dummyData = DummyData()

    var faultNo: String = ""

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
            reportStatus = "PROCESSING"
            submitFaultReport("submit", faultNo)
        }
        binding.btnSave.setOnClickListener {
            reportStatus = "DRAFT"
            submitFaultReport("save", faultNo)
//            Toast.makeText(requireContext(), "Fault Report Saved!", Toast.LENGTH_SHORT).show()
//            val direction = CreateFaultReportFragmentDirections.actionCreateFaultReportFragmentToHomeFragment()
//            this.findNavController().navigate(direction)
        }

        binding.btnUploadPhoto.setOnClickListener {
//            activity.openCamera(this, 1001)
            takePhotoAttachment()
        }

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

    private var imageUri: Uri? = null
    private fun takePhotoAttachment() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Attachment")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Att Desc")
        imageUri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)// Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)// Launch intent
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)// Callback from camera intent
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 1001){
                addPicture(imageUri)
            }
        }
        else {
            // Failed to take picture
        }
    }

    @SuppressLint("InflateParams")
    fun addPicture(uri: Uri?) {
        val imageViewLayout = layoutInflater.inflate(R.layout.attachment_picture, null)
        val imageView: ImageView = imageViewLayout.findViewById(R.id.iv)
//        imageView.setImageURI(uri)
        val bm = uriToBitmap(uri)
        imageView.setImageBitmap(bm)
        binding.llPictureUpload.addView(imageView)
    }

    private fun uriToBitmap(selectedFileUri: Uri?): Bitmap? {
        try {
            val parcelFileDescriptor: ParcelFileDescriptor? =
                selectedFileUri?.let { requireContext().contentResolver.openFileDescriptor(it, "r") }
            val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
            return Bitmap.createScaledBitmap(image, 200, 200, true)

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
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

    private fun setDropdownList(view: EditText?, data: ArrayList<String>?){
        if (data != null){
            val actView = (view as? AutoCompleteTextView)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, data)
            actView?.setAdapter(arrayAdapter)
        }
    }

    @SuppressLint("InflateParams")
    private fun addCheckboxBreakdown(breakdownItem: String){
        val checkboxLayout = layoutInflater.inflate(R.layout.breakdown_item_checkbox, null)
        val checkbox: Chip = checkboxLayout.findViewById(R.id.cbBreakdownItem)
        checkbox.text = breakdownItem

        checkbox.setOnClickListener{
            if (checkbox.isChecked){
                breakdownItemChecked.add(breakdownItem)
            } else {
                breakdownItemChecked.remove(breakdownItem)
            }
        }
        binding.cgBreakdownItem.addView(checkboxLayout)
    }

    fun addCheckboxBreakdown(breakdownItemList: ArrayList<String?>){
        binding.cgBreakdownItem.removeAllViews()
        breakdownItemChecked = ArrayList()
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

    private fun submitFaultReport(btn: String, faultNo: String?){
        val issueStatus = checkNull(binding.tilBreakdownDescription.editText)
//        val hourmeterStatus = checkNull(binding.tilHourmeter.editText)
        val hourmeterStatus = checkHourmeter()
        val contactNoStatus = checkNull(binding.tilContactNo.editText)
        val siteCodeStatus = checkNull(binding.tilSiteCode.editText)
        val assetIdStatus = checkNull(binding.tilAssetId.editText)
        val workingConditionStatus = checkNull(binding.tilWorkingCondition.editText)
        val accidentStatus = checkNull(binding.tilAccident.editText)
        val breakdownItemStatus = breakdownItemChecked.isNotEmpty()

        val listStatus = listOf(issueStatus, hourmeterStatus, contactNoStatus, siteCodeStatus, assetIdStatus, workingConditionStatus, accidentStatus, breakdownItemStatus)
        val predicate: (Boolean) -> Boolean = { it }
        val checkStatus = listStatus.all(predicate)
        if (checkStatus){
            val paramCreateFaultReport = ParamCreateFaultReport()
            paramCreateFaultReport.fault_no = faultNo
            paramCreateFaultReport.req_date = binding.tilDateAndTime.editText?.text.toString()
            paramCreateFaultReport.req_site = binding.tilSiteCode.editText?.text.toString()
            paramCreateFaultReport.requestor = binding.tilReportedBy.editText?.text.toString()
            paramCreateFaultReport.status = reportStatus
            paramCreateFaultReport.asset_category = userData?.getAssetCategory(binding.tilAssetId.editText?.text.toString())
            paramCreateFaultReport.asset_id = binding.tilAssetId.editText?.text.toString()
            paramCreateFaultReport.hourmeter = binding.tilHourmeter.editText?.text.toString()
            paramCreateFaultReport.work_condition = binding.tilWorkingCondition.editText?.text.toString()
            paramCreateFaultReport.issue = binding.tilBreakdownDescription.editText?.text.toString()
            paramCreateFaultReport.contact_person = "GET FROM DB"
            paramCreateFaultReport.contact_no = binding.tilContactNo.editText?.text.toString()
            paramCreateFaultReport.loc = "GET FROM DB"
            paramCreateFaultReport.submit_date = binding.tilDateAndTime.editText?.text.toString()
            paramCreateFaultReport.incident = binding.tilAccident.editText?.text.toString()
            paramCreateFaultReport.incident_rpt_track_no = ""
            paramCreateFaultReport.foreman = ""
            paramCreateFaultReport.wsho = ""
            paramCreateFaultReport.create_by = binding.tilReportedBy.editText?.text.toString()
            paramCreateFaultReport.breakdown_item = breakdownItemChecked

            userData?.submitFaultReport(requireContext(), paramCreateFaultReport, this, btn)
            loadingSubmit(true)
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Submission Error")
                .setMessage("Please check again. Make sure to fill the fields correctly.")
                .show()
        }
//        val direction = CreateFaultReportFragmentDirections.actionCreateFaultReportFragmentToHomeFragment()
//        this.findNavController().navigate(direction)
    }

    fun loadingSubmit(state: Boolean){
        if (state){
            binding.btnSave.visibility = GONE
            binding.btnSubmit.visibility = GONE
            binding.progressCircleSubmit.visibility = VISIBLE
        } else {
            binding.btnSave.visibility = VISIBLE
            binding.btnSubmit.visibility = VISIBLE
            binding.progressCircleSubmit.visibility = GONE
        }
    }

    @SuppressLint("SetTextI18n")
    fun changeFaultReportNumber(number: String?){
        faultNo = number.toString()
        binding.tvTitle.text = "Fault Report $faultNo"
        val rect = Rect(0, 0, requireView().width, requireView().height)
        view?.requestRectangleOnScreen(rect, false)

        Toast.makeText(requireContext(), "Fault Report Submitted!", Toast.LENGTH_SHORT).show()
    }

    fun changeFaultReportStatus(status: String){

        binding.tvStatus.text = status
        when (status){
            "PROCESSING" -> { binding.cvStatus.setBackgroundResource(R.drawable.shape_background_cardview_yellow) }
            "REJECTED" -> { binding.cvStatus.setBackgroundResource(R.drawable.shape_background_cardview_status_red) }
        }
    }

    fun disableAllView(state: Boolean, btn: String){
        for (children in requireView().allViews){
            for (child in children.allViews){

                if (child is Chip || child is TextView || child is CardView){
                    child.isClickable = !state
                    child.isFocusable = !state
                }
                else {
                    child.isEnabled = !state
                }

            }
        }
        when (btn) {
            "save" -> {
                binding.btnSave.isEnabled = !state
                binding.btnSubmit.isEnabled = state
                binding.btnSubmit.isClickable = state
                binding.btnSubmit.isFocusable = state
            }

            "submit" -> {
                binding.btnSubmit.isEnabled = !state
                binding.btnSave.isEnabled = !state
            }
        }
    }

    private fun checkNull(editText: EditText?): Boolean {
        return if (editText?.text.isNullOrBlank()){
            editText?.error = "Cannot be blank"
            false
        } else { true }
    }

    private fun checkHourmeter(): Boolean{
        val hourmeter_string = binding.tilHourmeter.editText?.text
        var hourmeter = 0
        if (hourmeter_string?.isNotBlank() == true && hourmeter_string.isNotEmpty()){
            hourmeter = try {
                hourmeter_string.toString().toInt()
            } catch (e: Exception){
                0
            }
        }
        val prev_hm = userData?.getAssetHourmeter(binding.tilAssetId.editText?.text.toString())
        return if ((prev_hm != null) && (hourmeter < prev_hm)){
            binding.tilHourmeter.editText?.error = "Previous Hourmeter is $prev_hm"
            false

        } else {
            true
        }


    }

}

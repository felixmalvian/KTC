package com.zenex.ktc.data

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.zenex.ktc.R
import com.zenex.ktc.api.RetrofitClient
import com.zenex.ktc.api.param.input.*
import com.zenex.ktc.api.param.response.*
import com.zenex.ktc.fragment.CreateFaultReportFragment
import com.zenex.ktc.fragment.FaultReportFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class UserData: Serializable {
    private var requestSiteList: Call<ParamGetSiteListResponse>? = null
    private var requestAssetList: Call<ParamGetAssetListResponse>? = null
    private var requestBreakdownItemList: Call<ParamGetBreakdownItemResponse>? = null
    private var requestFaultReportList: Call<ParamGetFaultReportListResponse>? = null

    private var requestCreateFaultReport: Call<ParamCreateFaultReportResponse>? = null


    var AC_LoginName: String? = null
    var AC_Username: String? = null
    var UR_UserRole: String? = null
    var EMP_No: String? = null

    var siteList: ArrayList<String>? = null
//    var assetList: ArrayList<ParamGetAssetListResponse.Data>? = null
    var assetList: ArrayList<String?>? = null

    var assetListDesc = mutableMapOf<String?, AssetDetails>()

    var faultReportNewList: ArrayList<ParamGetFaultReportListResponse.Data>? = ArrayList()
    var faultReportInProgressList: ArrayList<ParamGetFaultReportListResponse.Data>? = ArrayList()
    var faultReportCompletedList: ArrayList<ParamGetFaultReportListResponse.Data>? = ArrayList()


    var breakdownItemList = ArrayList<String?>()

    fun getSiteList(ctx: Context) {
        val paramEmpty = ParamEmpty()
        requestSiteList = RetrofitClient.instance.getSiteList(paramEmpty)
        requestSiteList?.enqueue(object: Callback<ParamGetSiteListResponse> {
            override fun onResponse(
                call: Call<ParamGetSiteListResponse>,
                response: Response<ParamGetSiteListResponse>
            ) {
                if(response.code() != 200){
                    AlertDialog.Builder(ctx)
                        .setTitle("Error")
                        .setMessage(response.code().toString())
                        .show()
                } else {
                    val data = response.body()!!.data
                    siteList = data
                }
            }

            override fun onFailure(call: Call<ParamGetSiteListResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    AlertDialog.Builder(ctx)
                        .setTitle("Error")
                        .setMessage(t.message)
                        .show()
                }
            }

        })
    }


    fun getAssetList(ctx: Context, site: String?, editText: EditText?){
        val paramAssetBySite = ParamAssetBySite()
        paramAssetBySite.site = site
        requestAssetList = RetrofitClient.instance.getAssetId(paramAssetBySite)
        requestAssetList?.enqueue(object: Callback<ParamGetAssetListResponse>{
            override fun onResponse(
                call: Call<ParamGetAssetListResponse>,
                response: Response<ParamGetAssetListResponse>
            ) {
                if(response.code() != 200){
                    AlertDialog.Builder(ctx)
                        .setTitle("Error")
                        .setMessage(response.code().toString())
                        .show()
                } else {
                    val body = response.body()!!
                    body.loadAssetList()
                    assetList = body.assetList
                    assetListDesc = body.assetListDesc

                    if (editText != null) {
                        val actView = (editText as? AutoCompleteTextView)
                        val arrayAdapter = ArrayAdapter(ctx, R.layout.dropdown_list, assetList!!)
                        actView?.setAdapter(arrayAdapter)
                    }
                }
            }

            override fun onFailure(call: Call<ParamGetAssetListResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    AlertDialog.Builder(ctx)
                        .setTitle("Error")
                        .setMessage(t.message)
                        .show()
                }
            }
        })
    }

    fun getAssetCategory(assetID: String): String? {
        val assetDetails = assetListDesc[assetID]
        return assetDetails?.category
    }

    fun getAssetHourmeter(assetID: String): Int? {
        val assetDetails = assetListDesc[assetID]
        return assetDetails?.hourmeter
    }

    fun getBreakdownItemList(ctx: Context, assetID: String, fragment: CreateFaultReportFragment?){
        val category = getAssetCategory(assetID)

        if (category != null) {
            val paramGetBreakdownItem = ParamGetBreakdownItem()
            paramGetBreakdownItem.EQM_Category = category

            requestBreakdownItemList = RetrofitClient.instance.getBreakdownItemList(paramGetBreakdownItem)
            requestBreakdownItemList?.enqueue(object: Callback<ParamGetBreakdownItemResponse>{
                override fun onResponse(
                    call: Call<ParamGetBreakdownItemResponse>,
                    response: Response<ParamGetBreakdownItemResponse>
                ) {
                    if(response.code() != 200){
                        AlertDialog.Builder(ctx)
                            .setTitle("Error")
                            .setMessage(response.code().toString())
                            .show()
                    } else {
                        val body = response.body()!!
                        body.loadBreakdownItemList()
                        breakdownItemList = body.breakdownItemList

                        if (fragment is CreateFaultReportFragment){
                            fragment.addCheckboxBreakdown(breakdownItemList)
                        }
                    }
                }

                override fun onFailure(call: Call<ParamGetBreakdownItemResponse>, t: Throwable) {
                    if (!call.isCanceled) {
                        AlertDialog.Builder(ctx)
                            .setTitle("Error")
                            .setMessage(t.message)
                            .show()
                    }
                }

            })
        }

    }

    fun submitFaultReport(ctx: Context, paramCreateFaultReport: ParamCreateFaultReport, fragment: Fragment, btn: String) {
        requestCreateFaultReport = RetrofitClient.instance.createFaultReport(paramCreateFaultReport)
        requestCreateFaultReport?.enqueue(object: Callback<ParamCreateFaultReportResponse>{
            override fun onResponse(
                call: Call<ParamCreateFaultReportResponse>,
                response: Response<ParamCreateFaultReportResponse>
            ) {
                if(response.code() != 200){
                    AlertDialog.Builder(ctx)
                        .setTitle("Error")
                        .setMessage(response.code().toString())
                        .show()
                } else {
                    val body = response.body()!!
                    val data = body.data
                    if (data != null){
                        if (fragment is CreateFaultReportFragment){
                            if (data.message_status == 0){
                                AlertDialog.Builder(ctx)
                                    .setTitle("Submit Failed")
                                    .setMessage(data.fault_status)
                                    .show()
                            } else {
                                fragment.changeFaultReportNumber(data.fault_no)
                                fragment.changeFaultReportStatus(data.fault_status!!)
                                fragment.disableAllView(true, btn)
                            }
                        }
                    }

                }

                if (fragment is CreateFaultReportFragment){
                    fragment.loadingSubmit(false)
                }
            }

            override fun onFailure(call: Call<ParamCreateFaultReportResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    AlertDialog.Builder(ctx)
                        .setTitle("Error")
                        .setMessage(t.message)
                        .show()
                }
                if (fragment is CreateFaultReportFragment){
                    fragment.loadingSubmit(false)
                }
            }

        })
    }


    private fun populateFaultReportList(fragment: Fragment, firstLoad: Boolean, currentScreen: String){
        if (fragment is FaultReportFragment){
            if (firstLoad){
                fragment.setFirstLoad(faultReportNewList)
            }

            fragment.setButtonToggleNew(faultReportNewList)
            fragment.setButtonToggleInProgress(faultReportInProgressList)
            fragment.setButtonToggleCompleted(faultReportCompletedList)

            when (currentScreen){
                "New" -> fragment.setRecyclerView(faultReportNewList, currentScreen)
                "In Progress" -> fragment.setRecyclerView(faultReportInProgressList, currentScreen)
                "Completed" -> fragment.setRecyclerView(faultReportCompletedList, currentScreen)
            }
        }
    }
    fun getFaultReportList(
        ctx: Context,
        fragment: Fragment,
        status: String,
        assetId: String,
        siteCode: String,
        dateFrom: String,
        dateTo: String,
        firstLoad: Boolean,
        currentScreen: String
    ) {
        if (faultReportNewList.isNullOrEmpty() || faultReportInProgressList.isNullOrEmpty() || faultReportCompletedList.isNullOrEmpty()){
            val statusSent = when (status) {
                "New" -> "DRAFT"
                "In Progress" -> "PROCESSING"
                "Completed" -> "COMPLETED"
                else -> ""
            }

            val paramGetFaultReportList = ParamGetFaultReportList()
            paramGetFaultReportList.status = statusSent
            paramGetFaultReportList.asset_id = assetId
            paramGetFaultReportList.site_code = siteCode
            paramGetFaultReportList.date_from = dateFrom
            paramGetFaultReportList.date_to = dateTo

            requestFaultReportList = RetrofitClient.instance.getFaultReportList(paramGetFaultReportList)
            requestFaultReportList?.enqueue(object: Callback<ParamGetFaultReportListResponse>{
                override fun onResponse(
                    call: Call<ParamGetFaultReportListResponse>,
                    response: Response<ParamGetFaultReportListResponse>
                ) {
                    if(response.code() != 200){
                        AlertDialog.Builder(ctx)
                            .setTitle("Error")
                            .setMessage(response.code().toString())
                            .show()
                    } else {
                        val body = response.body()!!
                        when (status){
                            "New" -> {faultReportNewList = body.data}
                            "In Progress" -> {faultReportInProgressList = body.data}
                            "Completed" -> {faultReportCompletedList = body.data}
                        }
                        populateFaultReportList(fragment, firstLoad, currentScreen)
                    }
                }

                override fun onFailure(call: Call<ParamGetFaultReportListResponse>, t: Throwable) {
                    if (!call.isCanceled) {
                        AlertDialog.Builder(ctx)
                            .setTitle("Error")
                            .setMessage(t.message)
                            .show()
                    }
                }

            })
        }
        else {
            populateFaultReportList(fragment, firstLoad, currentScreen)
        }
    }

}
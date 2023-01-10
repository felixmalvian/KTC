package com.zenex.ktc.data

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import com.zenex.ktc.R
import com.zenex.ktc.activity.BaseActivity
import com.zenex.ktc.api.RetrofitClient
import com.zenex.ktc.api.param.input.ParamAssetBySite
import com.zenex.ktc.api.param.input.ParamEmpty
import com.zenex.ktc.api.param.input.ParamGetBreakdownItem
import com.zenex.ktc.api.param.input.ParamLogin
import com.zenex.ktc.api.param.response.ParamGetAssetListResponse
import com.zenex.ktc.api.param.response.ParamGetBreakdownItemResponse
import com.zenex.ktc.api.param.response.ParamGetSiteListResponse
import com.zenex.ktc.fragment.CreateFaultReportFragment
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class UserData: Serializable {
    private var requestSiteList: Call<ParamGetSiteListResponse>? = null
    private var requestAssetList: Call<ParamGetAssetListResponse>? = null
    private var requestBreakdownItemList: Call<ParamGetBreakdownItemResponse>? = null

    private var requestCreateFaultReport: Call<ParamGetBreakdownItemResponse>? = null


    var AC_LoginName: String? = null
    var AC_Username: String? = null
    var UR_UserRole: String? = null
    var EMP_No: String? = null

    var siteList: ArrayList<String>? = null
//    var assetList: ArrayList<ParamGetAssetListResponse.Data>? = null
    var assetList: ArrayList<String?>? = null
    var assetListDesc = mutableMapOf("asset" to "category")
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


    fun getBreakdownItemList(ctx: Context, assetID: String, fragment: CreateFaultReportFragment?){
        val category = assetListDesc[assetID]

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

                        if (breakdownItemList.size > 0 && fragment is CreateFaultReportFragment){
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

}
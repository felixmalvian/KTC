package com.zenex.ktc.api.param.response

import com.zenex.ktc.data.AssetDetails

class ParamGetAssetListResponse {
    var data: ArrayList<Data>? = null
    var assetList = ArrayList<String?>()
    var assetListDesc = mutableMapOf<String?, AssetDetails>()

    class Data {
        var key: String? = null
        var label: String? = null
        var HourMeter: String? = null
    }

    fun loadAssetList(){
        for(item in data!!){
            assetList.add(item.key)
            val hourmeter_string = item.HourMeter.toString()
            var hourmeter = 0
            if (hourmeter_string.isNotBlank()){
                hourmeter = hourmeter_string.toInt()
            }
            assetListDesc[item.key] = AssetDetails(item.label, hourmeter)
        }
    }
}
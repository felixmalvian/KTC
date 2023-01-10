package com.zenex.ktc.api.param.response

class ParamGetAssetListResponse {
    var data: ArrayList<Data>? = null
    var assetList = ArrayList<String?>()
    var assetListDesc = mutableMapOf("asset" to "category")

    class Data {
        var key: String? = null
        var label: String? = null
        var HourMeter: String? = null
    }

    fun loadAssetList(){
        for(item in data!!){
            assetList.add(item.key)
            item.key?.let { item.label?.let { it1 -> assetListDesc.put(it, it1) } }
        }
    }
}
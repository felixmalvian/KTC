package com.zenex.ktc.api.param.response

class ParamGetBreakdownItemResponse {
    var data: ArrayList<Data>? = null
    var breakdownItemList = ArrayList<String?>()

    class Data {
        var key: String? = null
        var BRK_DOWN_ITM: String? = null
        var EQM_CATEGORY: String? = null
    }

    fun loadBreakdownItemList(){
        for(item in data!!){
            breakdownItemList.add(item.BRK_DOWN_ITM)
        }
    }
}
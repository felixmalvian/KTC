package com.zenex.ktc.api.param.response

class ParamGetFaultReportListResponse {
    var data: ArrayList<Data>? = null

    class Data {
        var id: String? = null
        var FR_No: String? = null
        var Req_Site: String? = null
        var Asset_Id: String? = null
        var Hourmeter: String? = null
        var Req_Date: String? = null
        var Status: String? = null

        var message_id: Int? = null
        var message_body: String? = null
    }
}
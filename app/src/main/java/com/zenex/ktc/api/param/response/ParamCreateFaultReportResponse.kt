package com.zenex.ktc.api.param.response

class ParamCreateFaultReportResponse {
    var data: Data? = null

    class Data {
        var message_status: Int = 0
        var fault_no: String? = null
        var fault_status: String? = null
    }
}
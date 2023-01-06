package com.zenex.ktc.api.param.response

class ParamLoginResponse {
    var data: List<Data>? = null

    class Data {
        var AC_LoginName: String? = null
        var AC_Username: String? = null
        var UR_UserRole: String? = null
        var EMP_No: String? = null
    }
}
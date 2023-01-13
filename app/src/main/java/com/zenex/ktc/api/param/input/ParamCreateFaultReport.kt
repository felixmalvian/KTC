package com.zenex.ktc.api.param.input

class ParamCreateFaultReport {
    var fault_no: String? = null

    var req_date: String? = null
    var req_site: String? = null
    var requestor: String? = null
    var status: String? = null
    var asset_category: String? = null
    var asset_id: String? = null
    var hourmeter: String? = null
    var work_condition: String? = null
    var issue: String? = null
    var contact_person: String? = null
    var contact_no: String? = null
    var loc: String? = null
    var submit_date: String? = null
    var incident: String? = null
    var incident_rpt_track_no: String? = null
    var foreman: String? = null
    var wsho: String? = null
    var create_by: String? = null
    var other_remarks: String? = null

    var breakdown_item: ArrayList<String>? = null
}
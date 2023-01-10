package com.zenex.ktc.data

class DummyData {
    val testItem = ArrayList<String>()
    fun addTestItem(){
        testItem.add("ART123")
        testItem.add("BR457")
        testItem.add("EX456")
        testItem.add("AC546")
        testItem.add("RD64")
        testItem.add("BR9")
    }

    val testSite = ArrayList<String>()
    fun addTestSite(){
        testSite.add("DSTAC02")
        testSite.add("DSTAC03")
        testSite.add("CCCCARC")
        testSite.add("HDBAMK1")
    }

    val yesNo = ArrayList<String>()
    fun addYesNo(){
        yesNo.add("Yes")
        yesNo.add("No")
    }
}
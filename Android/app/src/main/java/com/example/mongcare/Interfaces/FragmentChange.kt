package com.example.mongcare.Interfaces

interface FragmentChange {
    fun setFrag(fragNum : Int)
}
enum class PageName {
    MAIN,
    WALKTIME,
    AIRECOMMEND,
    SETTINGS,
    DIVICECONNECT,
    DIVICESTATUS,
    version
}
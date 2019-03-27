package com.summersama.fisimili.utils

import com.summersama.fisimili.data.SearchInfo

interface DataCallback {
    fun failure(msg:String)
    fun successful(searchInfo: SearchInfo)
}
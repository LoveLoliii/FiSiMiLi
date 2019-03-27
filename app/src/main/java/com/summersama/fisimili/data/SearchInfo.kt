package com.summersama.fisimili.data

import com.summersama.fisimili.data.IssuesInfo

class SearchInfo{
    var total_count:Int=0
    var incomplete_results:Boolean = false
    var items : List<IssuesInfo> =ArrayList<IssuesInfo>()
    override fun toString(): String {
        return "{total_count:"+total_count+",incomplete_results:"+incomplete_results+",items"+items+"}"
    }
}
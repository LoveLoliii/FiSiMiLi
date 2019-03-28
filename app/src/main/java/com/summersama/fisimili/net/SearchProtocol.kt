package com.summersama.fisimili.net

import com.summersama.fisimili.data.SearchInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface SearchProtocol {
    /*
    * 获取searchinfo
    * **/
    @GET("")
   fun getSearchInfo(): Call<Response<SearchInfo>>
}
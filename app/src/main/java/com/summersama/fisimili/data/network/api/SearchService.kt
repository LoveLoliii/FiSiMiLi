package com.summersama.fisimili.data.network.api

import com.summersama.fisimili.data.SearchInfo
import retrofit2.Call
import retrofit2.http.*

interface SearchService {


    @GET("search/issues")
    fun getSearchInfo(@Query("sort") sort:String,@Query("order")order:String,@Query("q",encoded = true)q:String): Call<SearchInfo>
    @GET()
    fun getSearchInfo(@Url url: String): Call<SearchInfo>

}

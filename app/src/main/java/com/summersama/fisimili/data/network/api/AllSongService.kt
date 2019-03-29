package com.summersama.fisimili.data.network.api

import androidx.paging.PagedList
import com.summersama.fisimili.data.IssuesInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface AllSongService {
    @GET()
    fun getIssuesInfo(@Url url: String): Call<List<IssuesInfo>>

}

package com.summersama.fisimili.data.network.api

import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.data.SearchInfo
import com.summersama.fisimili.data.SearchSongInfo
import com.summersama.fisimili.data.SongDownloadInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.lang.Exception

interface SongDetailService {

    @GET()
    fun getDownloadInfo(@Url url: String): Call<String>
    @GET()
    fun getPath(@Url url: String):Call<String>
    @GET
    fun getIssues(@Url url: String): Call<IssuesInfo>
}

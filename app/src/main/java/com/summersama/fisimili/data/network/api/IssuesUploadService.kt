package com.summersama.fisimili.data.network.api

import com.summersama.fisimili.data.IssuesInfo
import retrofit2.Call
import retrofit2.http.*

interface IssuesUploadService {
    @Headers("Accept:application/vnd.github.symmetra-preview+json")
    @POST()
   fun postIssues(@Url url: String,@Body map:HashMap<String,String>,@Header("Authorization")  access_token:String):Call<List<IssuesInfo>>
}

package com.summersama.fisimili.data

import android.os.HandlerThread
import android.util.Log
import com.google.gson.Gson
import com.summersama.fisimili.ui.search.SearchViewModel

import com.summersama.fisimili.utils.OkHttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.logging.Handler

class SearchRepository{
    object Instance  {
        val instance:SearchRepository= SearchRepository()
    }
    //val s = SearchRepository.Instance.instance
    fun getSearchResultOnline(url: String, searchViewModel: SearchViewModel): SearchInfo {
        var searchInfo: SearchInfo = SearchInfo()
        val call = OkHttpUtil.get(url)
        Log.d("getSearchResultOnline",Thread.currentThread().id.toString())
       call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
               Log.e("onF",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val result: String? = response.body()?.string();
                val g = Gson();
                Log.d("onResponse",Thread.currentThread().id.toString())
                searchInfo = g.fromJson(result, SearchInfo::class.java)
                Log.d("onR",result)
                searchViewModel.successful(searchInfo)
            }
        })
        Thread.sleep(1000)
       return searchInfo
    }

    fun getSearchResultOffline(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

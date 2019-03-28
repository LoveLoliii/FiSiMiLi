package com.summersama.fisimili.data

import android.util.Log
import com.summersama.fisimili.data.db.SearchDao
import com.summersama.fisimili.data.network.SearchNetwork

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository private constructor(private val searchDao: SearchDao, private val network: SearchNetwork){
    companion object {

        private var instance: SearchRepository? = null

        fun getInstance(searchDao: SearchDao, network: SearchNetwork): SearchRepository {
            if (instance == null) {
                synchronized(SearchRepository::class.java) {
                    if (instance == null) {
                        instance = SearchRepository(searchDao,network)
                    }
                }
            }
            return instance!!
        }

    }
    //val s = SearchRepository.Instance.instance
   suspend fun getSearchResultOnline(sort: String, order: String, q: String) = withContext(Dispatchers.IO) {
Log.d("url",sort+order+q)
val searchInfo = network.getSearchInfo(sort,order,q)

        searchInfo.items/*
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

            }
        })
        Thread.sleep(1000)
       return searchInfo*/
    }

    fun getSearchResultOffline(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

package com.summersama.fisimili.data

import android.util.Log
import com.summersama.fisimili.data.db.SearchDao
import com.summersama.fisimili.data.network.SearchNetwork
import com.summersama.fisimili.utils.FApplication
import com.summersama.fisimili.utils.FUtils

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
    lateinit var token:String
    lateinit var access_token:String

   suspend fun getSearchResultOnline(sort: String, order: String, q: String) = withContext(Dispatchers.IO) {
Log.d("url",sort+order+q)
       var x = "https://api.github.com/search/issues?q=$q+state:open+repo:zytx121/je&sort=$sort&order=$order"
       token = FUtils().getToken(ctx = FApplication.context,key = "token")
       if (token != ""){
           x="https://api.github.com/search/issues?access_token=$token&q=$q+state:open+repo:zytx121/je&sort=$sort&order=$order"
       }else{
           access_token = FUtils().getToken(ctx = FApplication.context,key = "access_token")
           if (access_token!=""){
               x="https://api.github.com/search/issues?access_token=$token&q=$q+state:open+repo:zytx121/je&sort=$sort&order=$order"
           }
       }
       Log.d("getIssues:", x)
val searchInfo = network.getSearchInfo(x)//network.getSearchInfo(sort,order,q)

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

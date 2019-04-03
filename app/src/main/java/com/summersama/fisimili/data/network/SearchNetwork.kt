package com.summersama.fisimili.data.network

import android.util.Log
import com.summersama.fisimili.data.network.api.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SearchNetwork {
    private val searchService = ServiceCreator.create(SearchService::class.java)
    suspend fun  getSearchInfo(sort: String,order:String,q:String) = searchService.getSearchInfo(sort,order,q).await()
   suspend fun getSearchInfo(url: String) = searchService.getSearchInfo(url).await()
    companion object {

        private var network: SearchNetwork? = null

        fun getInstance(): SearchNetwork {
            if (network == null) {
                synchronized(SearchNetwork::class.java) {
                    if (network == null) {
                        network = SearchNetwork()
                    }
                }
            }
            return network!!
        }

    }

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    Log.d("X-RateLimit-Remaining ",response.headers()["X-RateLimit-Remaining"]+" 状态码:"+response.code())
                    if (body != null) continuation.resume(body)

                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }


}

package com.summersama.fisimili.data.network

import android.util.Log
import com.summersama.fisimili.data.network.api.AllSongService
import com.summersama.fisimili.data.network.api.SongDetailService
import com.summersama.fisimili.utils.ConstantUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AllSongNetwork {
    private val allSongService = ServiceCreator.create(
        AllSongService::class.java
    )
    suspend fun   getIssuesInfo(url:String) = allSongService.getIssuesInfo(url).await()
    companion object {

        private var network: AllSongNetwork? = null

        open fun getInstance(): AllSongNetwork {
            if (network == null) {
                synchronized(SongDetailNetwork::class.java) {
                    if (network == null) {
                        network = AllSongNetwork()
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

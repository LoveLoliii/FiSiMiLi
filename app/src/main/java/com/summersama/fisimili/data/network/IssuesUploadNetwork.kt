package com.summersama.fisimili.data.network

import android.util.Log
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.data.network.api.IssuesUploadService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IssuesUploadNetwork {
    private val issuesUploadService = ServiceCreator.create(
        IssuesUploadService::class.java
    )
     suspend fun postIssues(url: String, map: HashMap<String, String>, token: String): List<IssuesInfo> = issuesUploadService.postIssues(url,map,token).await()
    companion object {

        private var network: IssuesUploadNetwork? = null

        open fun getInstance(): IssuesUploadNetwork {
            if (network == null) {
                synchronized(IssuesUploadNetwork::class.java) {
                    if (network == null) {
                        network = IssuesUploadNetwork()
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

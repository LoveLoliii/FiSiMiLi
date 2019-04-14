package com.summersama.fisimili.data

import android.util.Log
import com.summersama.fisimili.data.db.IssuesDao
import com.summersama.fisimili.data.network.IssuesUploadNetwork
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IssuesUploadRepository private constructor(private val issuesDao: IssuesDao, private val network: IssuesUploadNetwork)  {
    suspend fun uploadIssues(map: HashMap<String, String>) :IssuesInfo? = withContext(Dispatchers.IO)  {
        val url= "https://api.github.com/repos/loveloliii/ScoreS/issues"
        val token = FUtils().getToken(FNApplication.getContext(),"access_token")

        var rs :IssuesInfo = IssuesInfo()//allSongDao.getIssuesInfos(page,pageSize)
        // get from net
        rs =  network.postIssues(url,map, " Basic bG92ZWxvbGlpaTo3NTg1NDkxMjdHaXRIdWI=")
        // save to local
        Log.i("create issues result",rs.toString())
        rs



    }
    companion object {

        private var instance: IssuesUploadRepository? = null

        fun getInstance( issuesDao: IssuesDao,network: IssuesUploadNetwork): IssuesUploadRepository {
            if (instance == null) {
                synchronized(IssuesUploadRepository::class.java) {
                    if (instance == null) {
                        instance = IssuesUploadRepository(issuesDao = issuesDao, network = network)
                    }
                }
            }
            return instance!!
        }

    }
}

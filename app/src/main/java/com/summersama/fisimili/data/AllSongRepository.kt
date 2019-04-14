package com.summersama.fisimili.data

import com.summersama.fisimili.data.db.AllSongDao

import com.summersama.fisimili.data.network.AllSongNetwork
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllSongRepository private constructor(private val allSongDao: AllSongDao, private val network: AllSongNetwork) {
    lateinit var token :String
    lateinit var access_token :String
    val context = FNApplication.getContext()
    suspend   fun getAllSongInfo(page:Int,pageSize: Int):  List<IssuesInfo>? = withContext(Dispatchers.IO) {
        val url_source = FUtils().getToken(context,"url_source")
                var url = "https://api.github.com/repos/$url_source/issues?page=$page&per_page=$pageSize"
               token = FUtils().getToken(ctx = context,key = "token")

                if (  token != ""){
                    // 检查access_token

                    url="https://api.github.com/repos/$url_source/issues?access_token=$token&page=$page&per_page=$pageSize"
                }else{
                    access_token=FUtils().getToken(ctx = context,key = "access_token")
                    if (access_token != ""){
                        url="https://api.github.com/repos/$url_source/issues?access_token=$token&page=$page&per_page=$pageSize"

                    }
                }

                // try get data from local
                var rs :List<IssuesInfo> = ArrayList()//allSongDao.getIssuesInfos(page,pageSize)
                if (rs.isEmpty()){
                    // get from net
                    rs = network.getIssuesInfo(url)
                    // save to local
                }
                rs


    }

    companion object {

        private var instance: AllSongRepository? = null

        fun getInstance( allSongDao: AllSongDao,network: AllSongNetwork): AllSongRepository {
            if (instance == null) {
                synchronized(AllSongRepository::class.java) {
                    if (instance == null) {
                        instance = AllSongRepository(allSongDao,network)
                    }
                }
            }
            return instance!!
        }

    }

}

package com.summersama.fisimili.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.summersama.fisimili.data.db.AllSongDao

import com.summersama.fisimili.data.db.SearchDao
import com.summersama.fisimili.data.db.SongDetailDao
import com.summersama.fisimili.data.network.AllSongNetwork
import com.summersama.fisimili.data.network.SearchNetwork
import com.summersama.fisimili.data.network.SongDetailNetwork
import com.summersama.fisimili.utils.FApplication
import com.summersama.fisimili.utils.FUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllSongRepository private constructor(private val allSongDao: AllSongDao, private val network: AllSongNetwork) {
    lateinit var token :String
    lateinit var access_token :String
    suspend   fun getAllSongInfo(page:Int,pageSize: Int):  List<IssuesInfo>? = withContext(Dispatchers.IO) {
                var url = "https://api.github.com/repos/zytx121/je/issues?page=$page&per_page=$pageSize"
               token = FUtils().getToken(ctx = FApplication.context,key = "token")

                if (  token != ""){
                    // 检查access_token

                    url="https://api.github.com/repos/zytx121/je/issues?access_token=$token&page=$page&per_page=$pageSize"
                }else{
                    access_token=FUtils().getToken(ctx = FApplication.context,key = "access_token")
                    if (access_token != ""){
                        url="https://api.github.com/repos/zytx121/je/issues?access_token=$token&page=$page&per_page=$pageSize"

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

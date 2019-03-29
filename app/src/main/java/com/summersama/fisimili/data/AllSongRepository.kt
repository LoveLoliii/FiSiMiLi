package com.summersama.fisimili.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.summersama.fisimili.data.db.AllSongDao
import com.summersama.fisimili.data.db.PageDataSource.ASPDataSource
import com.summersama.fisimili.data.db.SearchDao
import com.summersama.fisimili.data.db.SongDetailDao
import com.summersama.fisimili.data.network.AllSongNetwork
import com.summersama.fisimili.data.network.SearchNetwork
import com.summersama.fisimili.data.network.SongDetailNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllSongRepository private constructor(private val allSongDao: AllSongDao, private val network: AllSongNetwork) {
    suspend   fun getAllSongInfo(page:Int,pageSize: Int):  List<IssuesInfo>? = withContext(Dispatchers.IO) {
                val url = "https://api.github.com/repos/zytx121/je/issues?page=$page&per_page=$pageSize"
                val rs = network.getIssuesInfo(url)
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

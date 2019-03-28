package com.summersama.fisimili.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.summersama.fisimili.data.db.SearchDao
import com.summersama.fisimili.data.db.SongDetailDao
import com.summersama.fisimili.data.network.SearchNetwork
import com.summersama.fisimili.data.network.SongDetailNetwork
import com.summersama.fisimili.utils.ConstantUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class SongDetailRepository private constructor(private val searchDao: SongDetailDao, private val network: SongDetailNetwork) {

    companion object {

        private var instance: SongDetailRepository? = null

        fun getInstance(songDao: SongDetailDao, network: SongDetailNetwork): SongDetailRepository {
            if (instance == null) {
                synchronized(SongDetailRepository::class.java) {
                    if (instance == null) {
                        instance = SongDetailRepository(songDao,network)
                    }
                }
            }
            return instance!!
        }

    }

    suspend fun getPath(key:String) = withContext(Dispatchers.IO){
        val url = ConstantUtils.MOLI_URL+"api/?callback=jQuery22408246496842419309_"  + System.currentTimeMillis().toString() + "&types=search&count=10&source=tencent&pages=1&name=" + URLEncoder.encode(key,"utf-8") + "&cache=9a94264bceaad353ef72684c2f01bb76&_=" + System.currentTimeMillis()
        Log.d("url",url)
        val p = network.getDownloadInfo(url);

        var s = p
        s = s.replaceBefore("[", "")
        s = s.replaceAfterLast("]", "")
        val g: Gson = Gson()

        val type = object : TypeToken<List<SearchSongInfo>>() {

        }.type
        // fixme 需要处理没有搜索到的情况
        val list: List<SearchSongInfo> = g.fromJson(s, type)
        if (list.size==0)
        ""
        else{
            val px= ConstantUtils.MOLI_URL+"api/?callback=jQuery22408246496842419309_"  + System.currentTimeMillis() + "&types=url&id=" + list[0].id + "&source=tencent&cache=9a94264bceaad353ef72684c2f01bb76&_=" + System.currentTimeMillis()
            var rs =network.getPath(px)
            rs = rs.replaceBefore("{", "")
            rs = rs.replaceAfterLast("}", "")

            val sdi= g.fromJson<SongDownloadInfo>(rs, SongDownloadInfo::class.java);
            sdi.url
        }
       /* if (p.size==0){
            ""
        }else{
            val path = network.getPath(p[0].url_id)
            path.url
        }*/
    }
}

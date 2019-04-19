package com.summersama.fisimili.utils

import com.summersama.fisimili.data.AllSongRepository
import com.summersama.fisimili.data.IssuesUploadRepository
import com.summersama.fisimili.data.SearchRepository
import com.summersama.fisimili.data.SongDetailRepository
import com.summersama.fisimili.data.db.*
import com.summersama.fisimili.data.network.AllSongNetwork
import com.summersama.fisimili.data.network.IssuesUploadNetwork
import com.summersama.fisimili.data.network.SearchNetwork
import com.summersama.fisimili.data.network.SongDetailNetwork
import com.summersama.fisimili.ui.searchall.AllSongModelFactory
import com.summersama.fisimili.ui.search.SearchModelFactory
import com.summersama.fisimili.ui.songdetail.SongDetailFactory
import com.summersama.fisimili.ui.upload.IssuesUploadModelFactory

object InjectorUtil {
    private fun getSearchRepository() = SearchRepository.getInstance(SearchInfoDatabase.getSearchDao(), SearchNetwork.getInstance())
    private fun getSongDetaiRepository() = SongDetailRepository.getInstance(SongDetailDatabase.getSongDetailDao(), SongDetailNetwork.getInstance())
    private fun getAllSongRepository() = AllSongRepository.getInstance(AllSongDatabase.getAllSongDao(), AllSongNetwork.getInstance())
    private fun getIssuesUploadRepository() = IssuesUploadRepository.getInstance(IssuesDatabase.getIssuesDao(),IssuesUploadNetwork.getInstance())
    fun getSearchModelFactory() = SearchModelFactory(getSearchRepository())
    fun getSongDetailModelFactory()= SongDetailFactory(getSongDetaiRepository())
    fun getAllSongModelFactory()= AllSongModelFactory(getAllSongRepository())
    fun getIssuesUploadModelFactory() = IssuesUploadModelFactory(getIssuesUploadRepository())
}

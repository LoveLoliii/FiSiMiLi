package com.summersama.fisimili.data.db

object SongDetailDatabase {
    private var SongDetailDao: SongDetailDao? = null



    fun getSongDetailDao(): SongDetailDao {
        if (SongDetailDao == null) SongDetailDao = SongDetailDao()
        return SongDetailDao!!
    }
}

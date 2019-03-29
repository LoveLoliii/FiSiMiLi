package com.summersama.fisimili.data.db

object AllSongDatabase {
    private var allSongDao: AllSongDao? = null



    fun getAllSongDao(): AllSongDao {
        if (allSongDao == null) allSongDao = AllSongDao()
        return allSongDao!!
    }
}

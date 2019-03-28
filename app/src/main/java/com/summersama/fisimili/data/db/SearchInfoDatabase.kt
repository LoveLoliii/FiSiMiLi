package com.summersama.fisimili.data.db

object SearchInfoDatabase {


    private var searchDao: SearchDao? = null



    fun getSearchDao(): SearchDao {
        if (searchDao == null) searchDao = SearchDao()
        return searchDao!!
    }
}

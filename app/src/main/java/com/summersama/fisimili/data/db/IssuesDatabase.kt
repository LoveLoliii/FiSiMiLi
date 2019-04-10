package com.summersama.fisimili.data.db

object IssuesDatabase {
    private var issuesDao: IssuesDao? = null



    fun getIssuesDao(): IssuesDao{
        if (issuesDao == null) issuesDao = IssuesDao()
        return issuesDao!!
    }}

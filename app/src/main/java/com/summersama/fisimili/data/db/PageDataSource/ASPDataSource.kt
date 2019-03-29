package com.summersama.fisimili.data.db.PageDataSource

import androidx.paging.ItemKeyedDataSource
import com.summersama.fisimili.data.IssuesInfo

class ASPDataSource(): ItemKeyedDataSource<IssuesInfo, List<IssuesInfo>>() {
    override fun loadInitial(params: LoadInitialParams<IssuesInfo>, callback: LoadInitialCallback<List<IssuesInfo>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAfter(params: LoadParams<IssuesInfo>, callback: LoadCallback<List<IssuesInfo>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<IssuesInfo>, callback: LoadCallback<List<IssuesInfo>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKey(item: List<IssuesInfo>): IssuesInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
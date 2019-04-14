package com.summersama.fisimili.ui.search


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summersama.fisimili.data.SearchRepository
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.data.SearchInfo
import com.summersama.fisimili.utils.FNApplication

import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
    val context = FNApplication.getContext()
    private var query = ""
    private var netState: Boolean = true
    private var searchInfo = SearchInfo()
    private var searchFragment = SearchFragment()
    var flag = false
    //  lateinit var issues: MutableLiveData<List<IssuesInfo>>
    var issues= MutableLiveData<List<IssuesInfo>>() /*by lazy {
        MutableLiveData<List<IssuesInfo>>().also {
            loadIssues()
        }
    }*/

    fun getIssues(query: String, netState: Boolean) {

        this.query = query
        // 网络状态
        this.netState = netState
        //  Thread.sleep(1000)
        if (this.netState) {
            loadIssues()
        }

    }


    private fun loadIssues() {
        // Do an asynchronous operation to fetch users.
        launch {
            //issues = MutableLiveData()
            Log.d("svm", query);
            var url = "sort=created&order=desc&q=$query+state:open+repo:zytx121/je"
            val sort = "created"
            val order = "desc"
            val q = "$query+state:open+repo:zytx121/je"
            //url = URLEncoder.encode(url,"utf-8")
            issues.value=(repository.getSearchResultOnline(sort,order,q))
        }


    }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            //isLoading.value = true
            //dataList.clear()
            block()
            //dataChanged.value = dataChanged.value?.plus(1)
            //isLoading.value = false
        } catch (t: Throwable) {
            t.printStackTrace()
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            //dataChanged.value = dataChanged.value?.plus(1)
            //isLoading.value = false
        }
    }
}

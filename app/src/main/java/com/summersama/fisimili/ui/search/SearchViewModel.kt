package com.summersama.fisimili.ui.search


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.summersama.je_a.entity.IssuesInfo
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.suspendCoroutine

class SearchViewModel : ViewModel() {
/*    private val _data = MutableLiveData<String>()
    val data:LiveData<String>
    get() =_data
    init {
        _data.value = "Jetpack"
    }*/
    var query = ""
    lateinit var  issues: MutableLiveData<List<IssuesInfo>>
    fun getIssues(query:String): LiveData<List<IssuesInfo>> {
        this.query = query
        //loadIssues()
        issues = MutableLiveData()
        return loadIssues()
    }
    private fun loadIssues(): LiveData<List<IssuesInfo>> {
        // Do an asynchronous operation to fetch users.
        Log.d("svm",query);
        var x = MutableLiveData<ArrayList<IssuesInfo>>()
         var v:ArrayList<IssuesInfo> = ArrayList<IssuesInfo>()
        var i:IssuesInfo = IssuesInfo()
        i.title = "1"
        v.add(i)
        issues.setValue(v);

     return issues
    }
}

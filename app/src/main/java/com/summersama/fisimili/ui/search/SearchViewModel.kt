package com.summersama.fisimili.ui.search


import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.summersama.fisimili.data.SearchRepository
import com.summersama.fisimili.utils.ConstantUtils
import com.summersama.fisimili.utils.DataCallback
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.data.SearchInfo

class SearchViewModel : ViewModel(),DataCallback {

    override fun failure(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun successful(searchInfo: SearchInfo) {
        Log.d("successful",searchInfo.toString())
        this.searchInfo =searchInfo
        this.flag = true
        searchFragment.successful(searchInfo)
    }


    /*    private val _data = MutableLiveData<String>()
        val data:LiveData<String>
        get() =_data
        init {
            _data.value = "Jetpack"
        }*/
    private var query = ""
    private var netState: Boolean = true
    private var searchInfo = SearchInfo()
    private var searchFragment=SearchFragment()
    var flag = false
    //  lateinit var issues: MutableLiveData<List<IssuesInfo>>
    private val issues: MutableLiveData<List<IssuesInfo>> by lazy {
        MutableLiveData<List<IssuesInfo>>().also {
            loadIssues()
        }
    }

    fun getIssues(query: String, netState: Boolean, searchFragment: SearchFragment) : MutableLiveData<List<IssuesInfo>>{
        this.query = query
        this.netState = netState
      //  Thread.sleep(1000)
        if(flag){
            loadIssues()
        }
        return issues
    }

    fun getIssuesData(): MutableLiveData<List<IssuesInfo>> {
        return issues
    }

    private fun loadIssues() {
        // Do an asynchronous operation to fetch users.
        Log.d("svm", query);
            val searchRepository = SearchRepository.Instance.instance

            var url = ConstantUtils.QUERY_URL + "&q=" + query + "+state:open+repo:zytx121/je"

            if (netState) {
                searchRepository.getSearchResultOnline(url,this)
             //  issues.value=searchInfo.items
             //   issues.value=searchInfo.items


            } else {
                searchRepository.getSearchResultOffline(query)
            }





    }

}

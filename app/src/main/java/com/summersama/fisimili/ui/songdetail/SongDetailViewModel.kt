package com.summersama.fisimili.ui.songdetail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.data.SearchRepository
import com.summersama.fisimili.data.SongDetailRepository
import com.summersama.fisimili.utils.FNApplication
import kotlinx.coroutines.launch

class SongDetailViewModel(private val repository: SongDetailRepository)  : ViewModel() {
    val context = FNApplication.getContext()
    var path  = MutableLiveData<String>()
    suspend fun getPath(key: String) {


                path.value= repository.getPath(key)





    }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (t: Throwable) {
            t.printStackTrace()
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun launchX(block: suspend () -> IssuesInfo) = viewModelScope.launch {
        try {
            block()
        } catch (t: Throwable) {
            t.printStackTrace()
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
        }
    }
    suspend fun getIssues(url: String) :IssuesInfo   {

               return  repository.getIssues(url)


    }
}

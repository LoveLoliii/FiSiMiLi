package com.summersama.fisimili.ui.all

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.summersama.fisimili.data.AllSongRepository
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.utils.FApplication
import kotlinx.coroutines.launch

class AllSongViewModel (private val allSongRepository: AllSongRepository): ViewModel() {

    var allSongList = MutableLiveData<List<IssuesInfo>>()
        //allSongRepository.getAllSongInfo(pageSize = 50)

    fun getList(mPage: Int) {
        launch {
            allSongList.value = allSongRepository.getAllSongInfo(page = mPage,pageSize = 50)

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
            Toast.makeText(FApplication.context, t.message, Toast.LENGTH_SHORT).show()
            //dataChanged.value = dataChanged.value?.plus(1)
            //isLoading.value = false
        }
    }
}

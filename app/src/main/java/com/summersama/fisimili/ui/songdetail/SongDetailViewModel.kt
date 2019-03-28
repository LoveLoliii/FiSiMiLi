package com.summersama.fisimili.ui.songdetail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.summersama.fisimili.data.SearchRepository
import com.summersama.fisimili.data.SongDetailRepository
import com.summersama.fisimili.utils.FApplication
import kotlinx.coroutines.launch

class SongDetailViewModel(private val repository: SongDetailRepository)  : ViewModel() {
    var path  = MutableLiveData<String>()
    fun getPath(key: String) {
        launch {

                path.value= repository.getPath(key)


        }


    }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (t: Throwable) {
            t.printStackTrace()
            Toast.makeText(FApplication.context, t.message, Toast.LENGTH_SHORT).show()
        }
    }
}

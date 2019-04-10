package com.summersama.fisimili.ui.upload

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.summersama.fisimili.data.IssuesUploadRepository
import com.summersama.fisimili.data.UploadSongInfo
import com.summersama.fisimili.utils.FApplication
import com.summersama.fisimili.utils.FUtils
import kotlinx.coroutines.launch

class IssuesUploadViewModel(private val issuesUploadRepository: IssuesUploadRepository) : ViewModel() {
    fun uploadData(us: UploadSongInfo) {
        // 构建markdown格式文本
        val body = FUtils().getBodyMarkDownText(us)
        val map = HashMap<String,String>(16)
        map["title"] = us.sn
        map["body"] = body
        launch  {
            Log.i("body",body)
            issuesUploadRepository.uploadIssues(map)
        }
    }

    val upstate  = MutableLiveData<Boolean>()
    private fun launch(block: suspend () -> Unit) = viewModelScope.launch{
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

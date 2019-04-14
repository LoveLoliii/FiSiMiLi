package com.summersama.fisimili.ui.token

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import kotlinx.coroutines.launch

class TokenManageViewModel : ViewModel() {
    val context = FNApplication.getContext()
    val token =MutableLiveData<String>()
      fun getSToken(ctx: Context) {

       token.value = FUtils().getToken(context,"token")


    }

    // TODO: Implement the ViewModel


    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (t: Throwable) {
            t.printStackTrace()
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setSToken(context: Context, value: String) {

        FUtils().saveToken(context,"token",value)

    }
}

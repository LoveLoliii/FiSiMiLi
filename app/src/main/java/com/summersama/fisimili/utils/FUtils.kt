package com.summersama.fisimili.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class FUtils {

    open fun getToken(ctx:Context,key:String):String{
        return ctx.getSharedPreferences(key, MODE_PRIVATE).getString(key, "")
    }

    fun saveToken(context: Context, key: String, value: String) {

        val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()//异步执行 过多 过大apply在结束activity时可能会造成application no response --> ANR

    }
}

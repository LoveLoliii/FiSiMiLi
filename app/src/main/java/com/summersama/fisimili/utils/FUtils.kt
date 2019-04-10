package com.summersama.fisimili.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.summersama.fisimili.data.UploadSongInfo
import java.lang.StringBuilder

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

    fun getBodyMarkDownText(us: UploadSongInfo): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("# ")
        stringBuilder.append(us.sn)
        stringBuilder.append("\r\n\r\n")
        stringBuilder.append("## 歌曲信息\r\n\r\n- ")
        stringBuilder.append("专辑：")
        stringBuilder.append(us.album)
        stringBuilder.append("\r\n")
        stringBuilder.append("作词：")
        stringBuilder.append(us.lyricist)
        stringBuilder.append("\r\n- ")
        stringBuilder.append("作曲：")
        stringBuilder.append(us.composer)
        stringBuilder.append("\r\n- ")
        stringBuilder.append("编曲：")
        stringBuilder.append(us.arrangement)
        stringBuilder.append("\r\n- ")
        stringBuilder.append("歌手：")
        stringBuilder.append(us.singer)
        stringBuilder.append("\r\n\r\n")
        stringBuilder.append("## 谱")
        stringBuilder.append("\r\n\r\n")
        stringBuilder.append("```")
        stringBuilder.append(us.nmn)
        stringBuilder.append("```")
        stringBuilder.append("\r\n\r\n")
        stringBuilder.append("记谱：")
        stringBuilder.append(us.wn)
        stringBuilder.append("\r\n\r\n")

        return stringBuilder.toString()
    }
}

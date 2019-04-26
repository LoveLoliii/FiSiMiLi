package com.summersama.fisimili.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.summersama.fisimili.data.UploadSongInfo
import java.lang.StringBuilder


open class FUtils {

    fun showAlert(ctx:Context,  info:String) {
    showAlert(ctx, info, null);
}

    open fun showAlert(ctx:Context, info: String, onDismiss: DialogInterface.OnDismissListener?) {
        AlertDialog.Builder(ctx)
            .setMessage(info)
            .setPositiveButton("sure", null)
            .setOnDismissListener(onDismiss)
            .show()
    }

    fun showToast(ctx:Context , msg:String ) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }



open fun getToken(ctx:Context,key:String):String{
    // 默认使用JE的仓库
        return ctx.getSharedPreferences(key, MODE_PRIVATE).getString(key, "")!!
    }
   open fun getToken(ctx: Context,name :String,key: String,dValue:String):String{
        return ctx.getSharedPreferences(name, MODE_PRIVATE).getString(key,dValue)!!
    }

    fun saveToken(context: Context, key: String, value: String) {

        val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()//异步执行 过多 过大apply在结束activity时可能会造成application no response --> ANR

    }

    fun getBodyMarkDownText(us: UploadSongInfo): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("# ")
        stringBuilder.append(us.sn)
        stringBuilder.append("\r\n\r\n")
        stringBuilder.append("## 歌曲信息\r\n\r\n- ")
        stringBuilder.append("专辑：")
        stringBuilder.append(us.album)
        stringBuilder.append("\r\n- ")
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
        stringBuilder.append("\r\n\r\n")
        stringBuilder.append("```")
        stringBuilder.append("\r\n\r\n")
        stringBuilder.append(" 记谱：")
        stringBuilder.append(us.wn)
        stringBuilder.append("\r\n\r\n")

        return stringBuilder.toString()
    }

    fun setColor(numHtml: String): String {
        var rs = numHtml

        rs = numHtml.replace("1","<font color=\"red\">1</font>")
        return rs
    }
    fun getRandomColor():String{
        return ""
    }
}

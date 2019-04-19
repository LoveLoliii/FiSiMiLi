package com.summersama.fisimili.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.summersama.fisimili.R
import com.summersama.fisimili.SearchActivity
import com.summersama.fisimili.ui.appintro.IntroActivity
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import com.summersama.fisimili.utils.TranslucentStatusUtil
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.top_bar_layout.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val topBar = ll_title_bar
        TranslucentStatusUtil.initState(this,topBar )
        //沉浸式状态栏适配
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // 底部导航栏 背景透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        val timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                if (FUtils().getToken(FNApplication.getContext(),"first")==""){
                    startAppIntro()
                }else{
                    startIndex()
                }
            }
        },1500)
    }

    private fun startIndex() {
        startActivity(Intent(this,SearchActivity::class.java))
        finish()
    }

    private fun startAppIntro() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }
}

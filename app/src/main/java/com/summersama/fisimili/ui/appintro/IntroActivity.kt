package com.summersama.fisimili.ui.appintro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntroFragment
import org.commonmark.internal.Bracket.image
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import android.R.attr.description
import android.R.attr.titleMarginBottom
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.model.SliderPage
import com.summersama.fisimili.R
import com.summersama.fisimili.SearchActivity
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import com.summersama.fisimili.utils.TranslucentStatusUtil
import kotlinx.android.synthetic.main.top_bar_layout.*




class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val topBar = ll_title_bar
        TranslucentStatusUtil.initState(this,topBar )
        //沉浸式状态栏适配
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // 底部导航栏 背景透明
        */window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        val sliderPage = SliderPage()
        sliderPage.title = "搜索"
        sliderPage.description = "搜索简谱使用了自由神社曲谱库2.0\n和一个自建的曲库。\n搜索默认不包含自建库，需要在搜索框左侧选择，\n同时这也是查看所有简谱的切换处。"
        sliderPage.imageDrawable = R.drawable.flogo
        sliderPage.bgColor = Color.parseColor("#1ba784")
        sliderPage.titleColor=Color.parseColor("#fbf2e3")
        sliderPage.descColor=Color.parseColor("#fbf2e3")
        //sliderPage.bgColor = R.color.龙葵紫
        addSlide(AppIntroFragment.newInstance(sliderPage))
        addSlide(AppIntroFragment.newInstance(SliderPage("上传","会上传到自建库：\nhttps://github.com/LoveLoliii/ScoreS\n",R.drawable.flogo, Color.parseColor("#1ba784"),Color.parseColor("#fbf2e3"),Color.parseColor("#fbf2e3"))))
        addSlide(AppIntroFragment.newInstance(SliderPage("github token","用于提高Api请求限制（非必须）\n可在github个人设置中创建一个无权限的token\n再到左侧进入github token页面保存",R.drawable.flogo, Color.parseColor("#1ba784"),Color.parseColor("#fbf2e3"),Color.parseColor("#fbf2e3"))))
        addSlide(AppIntroFragment.newInstance(SliderPage("成为偶像","只有想要成为艾欧泽亚偶像\n的种族才能使用！\n拉拉肥需要先下锅清洗干净！\n以上！\nover！",R.drawable.flogo, Color.parseColor("#1ba784"),Color.parseColor("#fbf2e3"),Color.parseColor("#fbf2e3"))))
        showSkipButton(false)

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // flag not first
        FUtils().saveToken(FNApplication.getContext(),"first","false")
        // first char must be * (not need
      //  FUtils().saveToken(FNApplication.getContext(),"collection","*")
        startActivity(Intent(this,SearchActivity::class.java))
        finish()
    }

}

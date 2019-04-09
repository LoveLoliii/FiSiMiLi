package com.summersama.fisimili

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.setupWithNavController
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.os.Build
import android.system.Os
import android.view.View
import com.summersama.fisimili.utils.FApplication
import com.summersama.fisimili.utils.TranslucentStatusUtil
import kotlinx.android.synthetic.main.top_bar_layout.*


class SearchActivity : AppCompatActivity() {
    object Instance{
        val instance:SearchActivity = SearchActivity()
    }
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            //设置状态栏颜色为透明
            window.statusBarColor = Color.TRANSPARENT
        }*/
        val topBar = ll_title_bar
        TranslucentStatusUtil.initState(this,topBar )
        //沉浸式状态栏适配
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.sf) as NavHostFragment? ?: return
       navController = host.navController


        setupNavigationMenu(navController)

     //   setupBottomNavMenu(navController)
        navController.navigate(R.id.searchFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
           /* Toast.makeText(this@SearchActivity, "Navigated to $dest",
                Toast.LENGTH_SHORT).show()*/
            Log.d("NavigationActivity", "Navigated to $dest")
        }

    }



    private fun setupNavigationMenu(navController: NavController) {
      val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

    }
}

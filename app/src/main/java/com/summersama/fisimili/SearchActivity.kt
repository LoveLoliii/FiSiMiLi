package com.summersama.fisimili

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PointF
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
import android.view.WindowManager
import com.summersama.fisimili.utils.TranslucentStatusUtil
import kotlinx.android.synthetic.main.nav_header_layout.*
import kotlinx.android.synthetic.main.top_bar_layout.*


class SearchActivity : AppCompatActivity() {
    object Instance{
        val instance:SearchActivity = SearchActivity()
    }
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val topBar = ll_title_bar
        TranslucentStatusUtil.initState(this,topBar )
        //沉浸式状态栏适配
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // 底部导航栏 背景透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.sf) as NavHostFragment? ?: return
       navController = host.navController
        setupNavigationMenu(navController)
      //  navController.navigate(R.id.searchFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
            Log.d("NavigationActivity", "Navigated to $dest")
        }

    }



    private fun setupNavigationMenu(navController: NavController) {
      val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

package com.summersama.fisimili

import android.app.Activity
import android.app.Application
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
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.summersama.fisimili.data.UpdateInfo
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import com.summersama.fisimili.utils.OkHttpUtil
import com.summersama.fisimili.utils.TranslucentStatusUtil
import kotlinx.android.synthetic.main.nav_header_layout.*
import kotlinx.android.synthetic.main.search_activity.*
import kotlinx.android.synthetic.main.top_bar_layout.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


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
        getUpdateInfo()
    }

    private fun getUpdateInfo() {
        val info = applicationContext.packageManager.getPackageInfo(applicationContext.packageName,0)
        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
           info.versionCode.toLong()
        }
        OkHttpUtil["https://github.com/LoveLoliii/FiSiMiLi/raw/master/app/release/update.json"].enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {

                val g = Gson()
                val updateInfo = g.fromJson(response.body()?.string(),UpdateInfo::class.java)
                val version_code = FUtils().getToken(ctx = FNApplication.getContext(),name="app_info",key="version_code",dValue = "1")
                if (version_code>= updateInfo.vercode){
                    //nothing
                }else{
                    // show dialog
                    Snackbar.make(nav_view,"123",Snackbar.LENGTH_SHORT)
                        .setAction("下载") {
                        Log.e("？","snackbar down")

                        }
                        .show()
                }
            }
        })
    }


    private fun setupNavigationMenu(navController: NavController) {
      val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

package com.summersama.fisimili.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.summersama.fisimili.R
import com.summersama.fisimili.adapter.SongListAdapter
import com.summersama.fisimili.data.IssuesInfo
import kotlinx.android.synthetic.main.search_fragment.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.summersama.fisimili.SearchActivity
import com.summersama.fisimili.data.UpdateInfo
import com.summersama.fisimili.ui.service.DownloadService
import com.summersama.fisimili.utils.*
import kotlinx.android.synthetic.main.back_ball_layout.*
import kotlinx.android.synthetic.main.search_activity.*
import kotlinx.android.synthetic.main.update_dialog.*
import kotlinx.android.synthetic.main.update_dialog.view.*
import me.shaohui.bottomdialog.BottomDialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class SearchFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter();
        filter.addAction("updateR");
        try {

            activity?.registerReceiver(mReceiver, filter);
        } catch (e: Exception) {
            // Log.i(TAG, "onResume: "+e);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            activity?.unregisterReceiver(mReceiver);
        } catch (e: Exception) {
            // Log.i(TAG, "onPause: "+e);
        }
    }


    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //更新进度
            val progress = intent.getIntExtra("progress", 0)
            //progressBar.setProgress(progress);
            dialog?.setProgress(progress)
            if (progress == 100) {
                Toast.makeText(getContext(), "下载完成", Toast.LENGTH_SHORT).show()
                //关闭进度条
                dialog?.dismiss()
            }
        }
    }


    companion object {
        fun newInstance() = SearchFragment()
    }

    lateinit var issues: MutableLiveData<List<IssuesInfo>>
    lateinit var viewModel: SearchViewModel
    lateinit var adapter: SongListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this.activity!!, InjectorUtil.getSearchModelFactory())
            .get(SearchViewModel::class.java)
        init()
        observe()
    }

    private fun observe() {

        viewModel.issues.observe(this, Observer { issues ->
            Log.d("issue change", issues.toString())
            // adapter.notifyDataSetChanged()

            sf_spin_kit.visibility = View.GONE
            if (issues.isNotEmpty()) {
                Log.d(activity?.localClassName, issues[0].title)
            }
            adapter = SongListAdapter(issues, ctx = this.context!!)
            val layoutManager = object : LinearLayoutManager(activity) {
            }
            layoutManager.orientation = RecyclerView.VERTICAL
            am_recycle_view.layoutManager = layoutManager
            am_recycle_view.adapter = adapter
        })

    }

    private fun getUpdateInfo() {
        val info = context?.packageManager?.getPackageInfo(context!!.packageName, 0)
        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info?.longVersionCode
        } else {
            info?.versionCode?.toLong()
        }
        OkHttpUtil["https://github.com/LoveLoliii/FiSiMiLi/raw/master/app/release/update.json"].enqueue(object :
            Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {

                val g = Gson()
                val updateInfo = g.fromJson(response.body()?.string(), UpdateInfo::class.java)
                val version_code = FUtils().getToken(
                    ctx = FNApplication.getContext(),
                    name = "app_info",
                    key = "version_code",
                    dValue = "1"
                )
                if (version_code > updateInfo.vercode) {
                    //nothing
                } else {
                    // show dialog
                    FNApplication.topActivity().runOnUiThread{
                       // Toast.makeText(a, getString(R.string.find_new_version), Toast.LENGTH_SHORT).show()
                        val builder = AlertDialog.Builder(FNApplication.topActivity())
                        val inflater = activity!!.layoutInflater
                        val v1 = inflater.inflate(R.layout.update_dialog, null)
                        val  log = v1.ud_log_tx
                        log.text = updateInfo.updateContent.replace("\\n", "\n")
                        builder
                            .setView(v1)
                            .setPositiveButton("更新") { dialog, which ->
                                startDown()
                            }
                            .setNegativeButton("取消"
                            ) { dialog, _ -> dialog?.cancel() }
                            .setTitle("发现新版本");
                        builder.create()
                        builder.show()
                    }

                }
            }
        })
    }

    private var dialog: ProgressDialog? = ProgressDialog(FNApplication.getContext())

    private fun init() {
        getUpdateInfo()
        sf_fish_fdv.setFishLongClickListener {
            startDown()
            true
        }
        /* val ivFish =  iv_fish  as ImageView
         ivFish.setImageDrawable(FishDrawable(context!!))*/
        randomWaterBallAnimation()
        val id: Int = am_query_input.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = am_query_input.findViewById<TextView>(id)
        val spid = am_query_input.context.resources.getIdentifier("android:id/search_plate", null, null)
        val mSearchPlate = am_query_input.findViewById<View>(spid)
        val said = am_query_input.context.resources.getIdentifier("android:id/submit_area", null, null)
        val mSubmitArea = am_query_input.findViewById<View>(said)
        mSearchPlate.setBackgroundColor(Color.TRANSPARENT)
        mSubmitArea.setBackgroundColor(Color.TRANSPARENT)
        textView.setTextColor(resources.getColor(R.color.heyelv))
        am_query_input.setIconifiedByDefault(false)
        am_query_input.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(activity, newText, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // 清空recycleview
                adapter = SongListAdapter(ArrayList(), ctx = context!!)
                val layoutManager = object : LinearLayoutManager(activity) {
                }
                layoutManager.orientation = RecyclerView.VERTICAL
                am_recycle_view.layoutManager = layoutManager
                am_recycle_view.adapter = adapter
                //add load animation
                sf_spin_kit.visibility = View.VISIBLE
                getSearchResult(query.toString())
                return false
            }

        })
        sf_select_iv.setOnClickListener {
            BottomDialog.create(fragmentManager)
                .setViewListener {
                    // // You can do any of the necessary the operation with the view
                    val je = it.findViewById<TextView>(R.id.dsl_je_tx)
                    val ll = it.findViewById<TextView>(R.id.dsl_ll_tx)
                    je.setOnClickListener {
                        FUtils().saveToken(context = context!!, key = "url_source", value = "zytx121/je")
                        Log.i("searchFragment", "save je shpf")
                        val prev = fragmentManager!!.findFragmentByTag("BottomDialog")
                        if (prev != null) {
                            val df = prev as BottomDialog
                            df.dismiss()
                        }
                    }
                    ll.setOnClickListener {
                        FUtils().saveToken(context = context!!, key = "url_source", value = "loveloliii/ScoreS")
                        Log.i("searchFragment", "save ll shpf")
                        val prev = fragmentManager!!.findFragmentByTag("BottomDialog")
                        if (prev != null) {
                            val df = prev as BottomDialog
                            df.dismiss()
                        }
                    }

                }
                .setLayoutRes(R.layout.dialog_select_layout)
                .setDimAmount(0.1f)            // Dialog window dim amount(can change window background color）, range：0 to 1，default is : 0.2f
                .setCancelOutside(true)     // click the external area whether is closed, default is : true
                .setTag("BottomDialog")     // setting the DialogFragment tag
                .show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                print(requestCode)
                Toast.makeText(context, "long click", Toast.LENGTH_SHORT).show()
            }
            2 -> print(requestCode)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun startDown() {

        // FUtils().showAlert(context!!, "")
        Toast.makeText(context, "downloading last version app ......", Toast.LENGTH_LONG).show()

        //权限判断是否有访问外部存储空间权限
        val flag = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (flag != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                Toast.makeText(context, "permissions need", Toast.LENGTH_LONG).show()
            } else {
                // 申请授权。
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity?.requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                }
            }
        } else {

            val intent = Intent(context, DownloadService::class.java)
            //携带额外数据
            intent.putExtra("url", "https://github.com/LoveLoliii/FiSiMiLi/raw/master/app/release/app-release.apk")
            //发送数据给service
            FNApplication.getContext().startService(intent)
            //创建下载进度条
            dialog = ProgressDialog(context)
            //设置最大值
            dialog!!.setMax(100)
            //设置为水平
            dialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            dialog!!.setTitle("下载中......")
            // 不让他
            dialog!!.setCanceledOnTouchOutside(false)
            //显示进度条
            dialog!!.show()

        }
    }

    private fun getSearchResult(query: String) {
        viewModel.getIssues(query, true)
    }

    private fun randomWaterBallAnimation() {
        val mAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.3f
        )
        mAnimation.duration = 25000
        mAnimation.repeatCount = -1
        mAnimation.repeatMode = Animation.REVERSE
        mAnimation.interpolator = LinearInterpolator()
        water_ball.animation = mAnimation


        val mAnimation2 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.7f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.2f
        )
        mAnimation2.duration = 30000
        mAnimation2.repeatCount = -1
        mAnimation2.repeatMode = Animation.REVERSE
        mAnimation2.interpolator = LinearInterpolator()
        water_ball_2.animation = mAnimation2


        val mAnimation3 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.8f
        )
        mAnimation3.duration = 25000
        mAnimation3.repeatCount = -1
        mAnimation3.repeatMode = Animation.REVERSE
        mAnimation3.interpolator = LinearInterpolator()
        water_ball_3.animation = mAnimation3


        val mAnimation4 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation4.duration = 45000
        mAnimation4.repeatCount = -1
        mAnimation4.repeatMode = Animation.REVERSE
        mAnimation4.interpolator = LinearInterpolator()
        water_ball_4.animation = mAnimation4


        val mAnimation5 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.3f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.9f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1f
        )
        mAnimation5.duration = 20000
        mAnimation5.repeatCount = -1
        mAnimation5.repeatMode = Animation.REVERSE
        mAnimation5.interpolator = LinearInterpolator()
        water_ball_5.animation = mAnimation5


        val mAnimation6: TranslateAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation6.duration = 35000
        mAnimation6.repeatCount = -1
        mAnimation6.repeatMode = Animation.REVERSE
        mAnimation6.interpolator = LinearInterpolator()
        water_ball_6.animation = mAnimation6


        val mAnimation7: TranslateAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.8f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.6f
        )
        mAnimation7.duration = 28000
        mAnimation7.repeatCount = -1
        mAnimation7.repeatMode = Animation.REVERSE
        mAnimation7.interpolator = LinearInterpolator()
        water_ball_7.animation = mAnimation7


        val mAnimation8 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.4f
        )
        mAnimation8.duration = 21000
        mAnimation8.repeatCount = -1
        mAnimation8.repeatMode = Animation.REVERSE
        mAnimation8.interpolator = LinearInterpolator()
        water_ball_8.animation = mAnimation8


        val mAnimation10 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.2f
        )
        mAnimation10.duration = 25000
        mAnimation10.repeatCount = -1
        mAnimation10.repeatMode = Animation.REVERSE
        mAnimation10.interpolator = LinearInterpolator()
        water_ball_10.animation = mAnimation10


        val mAnimation9 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.4f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.3f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation9.duration = 31000
        mAnimation9.repeatCount = -1
        mAnimation9.repeatMode = Animation.REVERSE
        mAnimation9.interpolator = LinearInterpolator()
        water_ball_9.animation = mAnimation9
    }
}

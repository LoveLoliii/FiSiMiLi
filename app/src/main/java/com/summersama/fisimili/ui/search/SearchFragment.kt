package com.summersama.fisimili.ui.search

import android.app.Activity
import android.graphics.Color
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
import com.summersama.fisimili.utils.DataCallback
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.data.SearchInfo
import com.summersama.fisimili.utils.InjectorUtil
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment(){



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


        viewModel = ViewModelProviders.of(this.activity!!, InjectorUtil.getSearchModelFactory()).get(SearchViewModel::class.java)
        init();
        observe()
    }

    private fun observe() {

        viewModel.issues.observe(this, Observer {
                issues->
            Log.d("issue change",issues.toString())
           // adapter.notifyDataSetChanged()
            if (issues.isNotEmpty()){
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

    private fun init() {
        randomWaterBallAnimation();
        val id: Int = am_query_input.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = am_query_input.findViewById<TextView>(id)
        textView.setTextColor(Color.parseColor("#66ccff"))
        am_query_input.setIconifiedByDefault(false)
        am_query_input.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(activity, newText, Toast.LENGTH_SHORT).show();
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                getSearchResult(query.toString())
                return false
            }

        })
    }

    private fun getSearchResult(query: String) {
        // 重新搜索
       /* viewModel.getIssues(query.also { Log.d("iss", query) }, true, this)
             .observe(this, Observer<List<IssuesInfo>> { issues ->
                // update UI
                viewModel.issues.postValue(issues)

            })*/
        viewModel.getIssues(query,true)
       /* viewModel.issues.observe(this, Observer {
                issues->
            Log.d("issue change",issues.toString())
            adapter.notifyDataSetChanged()
            Log.d(activity?.localClassName, issues[0].title)
            adapter = SongListAdapter(issues, ctx = this.context!!)
            val layoutManager = object : LinearLayoutManager(activity) {
            }
            layoutManager.orientation = RecyclerView.VERTICAL
            am_recycle_view.layoutManager = layoutManager
            am_recycle_view.adapter = adapter
        })*/
    }

    private fun randomWaterBallAnimation() {

        val mAnimation: TranslateAnimation
        mAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.3f
        )
        mAnimation.duration = 25000
        mAnimation.repeatCount = -1
        mAnimation.repeatMode = Animation.REVERSE
        mAnimation.interpolator = LinearInterpolator()
        water_ball.setAnimation(mAnimation)


        val mAnimation2: TranslateAnimation
        mAnimation2 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.7f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.2f
        )
        mAnimation2.duration = 30000
        mAnimation2.repeatCount = -1
        mAnimation2.repeatMode = Animation.REVERSE
        mAnimation2.interpolator = LinearInterpolator()
        water_ball_2.setAnimation(mAnimation2)


        val mAnimation3: TranslateAnimation
        mAnimation3 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.8f
        )
        mAnimation3.duration = 25000
        mAnimation3.repeatCount = -1
        mAnimation3.repeatMode = Animation.REVERSE
        mAnimation3.interpolator = LinearInterpolator()
        water_ball_3.setAnimation(mAnimation3)


        val mAnimation4: TranslateAnimation
        mAnimation4 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 1.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation4.duration = 45000
        mAnimation4.repeatCount = -1
        mAnimation4.repeatMode = Animation.REVERSE
        mAnimation4.interpolator = LinearInterpolator()
        water_ball_4.setAnimation(mAnimation4)


        val mAnimation5: TranslateAnimation
        mAnimation5 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.3f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.9f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1f
        )
        mAnimation5.duration = 20000
        mAnimation5.repeatCount = -1
        mAnimation5.repeatMode = Animation.REVERSE
        mAnimation5.interpolator = LinearInterpolator()
        water_ball_5.setAnimation(mAnimation5)


        val mAnimation6: TranslateAnimation
        mAnimation6 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation6.duration = 35000
        mAnimation6.repeatCount = -1
        mAnimation6.repeatMode = Animation.REVERSE
        mAnimation6.interpolator = LinearInterpolator()
        water_ball_6.setAnimation(mAnimation6)


        val mAnimation7: TranslateAnimation
        mAnimation7 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.8f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.1f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.6f
        )
        mAnimation7.duration = 28000
        mAnimation7.repeatCount = -1
        mAnimation7.repeatMode = Animation.REVERSE
        mAnimation7.interpolator = LinearInterpolator()
        water_ball_7.setAnimation(mAnimation7)


        val mAnimation8: TranslateAnimation
        mAnimation8 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.4f
        )
        mAnimation8.duration = 21000
        mAnimation8.repeatCount = -1
        mAnimation8.repeatMode = Animation.REVERSE
        mAnimation8.interpolator = LinearInterpolator()
        water_ball_8.setAnimation(mAnimation8)


        val mAnimation10: TranslateAnimation
        mAnimation10 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.2f
        )
        mAnimation10.duration = 25000
        mAnimation10.repeatCount = -1
        mAnimation10.repeatMode = Animation.REVERSE
        mAnimation10.interpolator = LinearInterpolator()
        water_ball_10.setAnimation(mAnimation10)


        val mAnimation9: TranslateAnimation
        mAnimation9 = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0.4f,
            TranslateAnimation.RELATIVE_TO_PARENT, 0.3f,
            TranslateAnimation.RELATIVE_TO_PARENT, 1.2f,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.1f
        )
        mAnimation9.duration = 31000
        mAnimation9.repeatCount = -1
        mAnimation9.repeatMode = Animation.REVERSE
        mAnimation9.interpolator = LinearInterpolator()
        water_ball_9.setAnimation(mAnimation9)
    }
}

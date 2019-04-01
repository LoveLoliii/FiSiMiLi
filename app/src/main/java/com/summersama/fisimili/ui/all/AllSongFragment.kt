package com.summersama.fisimili.ui.all

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.summersama.fisimili.R
import com.summersama.fisimili.adapter.AllSongAdapter
import com.summersama.fisimili.adapter.SongListAdapter
import com.summersama.fisimili.utils.InjectorUtil
import kotlinx.android.synthetic.main.all_song_fragment.*
import java.lang.Exception

class AllSongFragment : Fragment() {
    lateinit var adapter: AllSongAdapter
    var mPage:Int = 1
    companion object {
        fun newInstance() = AllSongFragment()
    }

    private lateinit var viewModel: AllSongViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_song_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(context,"第 $mPage 页",Toast.LENGTH_SHORT).show()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get page
        val bundle = arguments

        try {
              mPage = bundle!!.getInt("page")
        }catch (e:Exception){
            mPage = 1
        }
        Toast.makeText(context,"第 $mPage 页",Toast.LENGTH_SHORT).show()
        // loading animation

        viewModel = ViewModelProviders.of(this,InjectorUtil.getAllSongModelFactory()).get(AllSongViewModel::class.java)
        viewModel.allSongList.observe(this, Observer {
            Log.d(this.javaClass.canonicalName, it[0].node_id)
            spin_kit.visibility=View.GONE

            //asf_recycle_view.visibility = RecyclerView.VISIBLE //View.VISIBLE
            adapter =AllSongAdapter(mPage,it,context!!)
            val layoutManager = object : LinearLayoutManager(activity) {
            }
            layoutManager.orientation = RecyclerView.VERTICAL
            asf_recycle_view.layoutManager = layoutManager
            asf_recycle_view.adapter = adapter
        })
        viewModel.getList(mPage)
    }

}

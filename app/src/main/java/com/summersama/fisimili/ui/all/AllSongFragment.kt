package com.summersama.fisimili.ui.all

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.summersama.fisimili.R
import com.summersama.fisimili.adapter.AllSongAdapter
import com.summersama.fisimili.adapter.SongListAdapter
import com.summersama.fisimili.utils.InjectorUtil
import kotlinx.android.synthetic.main.all_song_fragment.*

class AllSongFragment : Fragment() {
    lateinit var adapter: SongListAdapter
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,InjectorUtil.getAllSongModelFactory()).get(AllSongViewModel::class.java)
        viewModel.allSongList.observe(this, Observer {
            Log.d(this.javaClass.canonicalName, it[0].node_id)
            adapter =SongListAdapter(it,context!!)
            val layoutManager = object : LinearLayoutManager(activity) {
            }
            layoutManager.orientation = RecyclerView.VERTICAL
            asf_recycle_view.layoutManager = layoutManager
            asf_recycle_view.adapter = adapter
        })
        viewModel.getList()
    }

}

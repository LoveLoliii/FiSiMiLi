package com.summersama.fisimili.ui.songdetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.summersama.fisimili.R

class SongDetailFragment : Fragment() {

    companion object {
        fun newInstance() = SongDetailFragment()
    }

    private lateinit var viewModel: SongDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       Log.d("s","sss")
        return inflater.inflate(R.layout.song_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SongDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

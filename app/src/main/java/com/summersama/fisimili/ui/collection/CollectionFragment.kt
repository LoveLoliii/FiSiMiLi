  package com.summersama.fisimili.ui.collection

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.summersama.fisimili.R
import com.summersama.fisimili.adapter.CollectionAdapter
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import kotlinx.android.synthetic.main.collection_fragment.*

  class CollectionFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionFragment()
    }

    private lateinit var viewModel: CollectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.collection_fragment, container, false)
    }
      lateinit var adapter: CollectionAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CollectionViewModel::class.java)
        var iCollect = FUtils().getToken(FNApplication.getContext(),"collection").split("*").toMutableList()
        var uCollect = FUtils().getToken(FNApplication.getContext(),"url_collect").split("*").toMutableList()
        iCollect = iCollect.subList(1,iCollect.size)
        uCollect = uCollect.subList(1,uCollect.size)
         iCollect.addAll(uCollect)

        val collect = iCollect
        adapter = CollectionAdapter(collect,context!!)
        val layoutManager = object : LinearLayoutManager(activity) {
        }
        layoutManager.orientation = RecyclerView.VERTICAL
        cf_recycle_view.layoutManager = layoutManager
        cf_recycle_view.adapter = adapter

    }

}

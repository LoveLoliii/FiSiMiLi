package com.summersama.fisimili.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.summersama.fisimili.data.IssuesInfo
//(it: PagedList<IssuesInfo>?, ctx: Context)
class AllSongAdapter :PagedListAdapter<IssuesInfo,  RecyclerView.ViewHolder>(DIFF_CALLBACK)  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val concert = getItem(position)

        // Note that "concert" is a placeholder if it's null.
       // holder.bindTo(concert)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback< IssuesInfo >() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert:IssuesInfo,
                                         newConcert:IssuesInfo) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(oldConcert: IssuesInfo,
                                            newConcert: IssuesInfo) = oldConcert.id == newConcert.id
        }
    }
}

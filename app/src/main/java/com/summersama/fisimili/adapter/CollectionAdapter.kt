package com.summersama.fisimili.adapter


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.summersama.fisimili.R
import com.summersama.fisimili.data.IssuesInfo
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils

class CollectionAdapter(val list:List<String>, val ctx: Context) :
    RecyclerView.Adapter<CollectionAdapter.CollectionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false);
        return CollectionHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        if(list.size-1 == position){

        }
        var url = list[position];
        if (!url.contains("@")){
            url = list[position]+"@"+list[position]
        }
        val ul = url.split("@")
        holder.textView.text =ul[1]
        //  holder.bodyTx.setText(list[position].body)
        holder.textView.setOnClickListener{
            val navController = Navigation.findNavController(ctx as Activity, R.id.sf)
                if (url.contains("https://api.github.com")){
                    val bundle:Bundle = Bundle()
                    bundle.putString("url",ul[0])
                    navController.navigate(R.id.songDetailFragment,bundle)
                }else /*if(list[position].contains("http://m.qinyipu.com"))*/{
                    val bundle = Bundle()
                    bundle.putString("url",ul[0])
                    navController.navigate(R.id.qinyipu,bundle)
                }

        }
        holder.textView.setOnLongClickListener {
            var collectXml = FUtils().getToken(FNApplication.getContext(),"url_collect")
            collectXml = collectXml.replace("*${list[position]}","")
            FUtils().saveToken(FNApplication.getContext(),"url_collect",collectXml)
            notifyItemRemoved(position);
            notifyDataSetChanged()
            Toast.makeText(FNApplication.getContext(),"已删除",Toast.LENGTH_SHORT).show()
            true
        }
    }

    class CollectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView:TextView = itemView.findViewById(R.id.rvi_title_tx)
        //  val bodyTx:TextView = itemView.findViewById(R.id.rvi_body_tx)
    }

}
package com.summersama.fisimili.adapter


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.summersama.fisimili.R
import com.summersama.fisimili.data.IssuesInfo

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
        holder.textView.text = list[position]
        //  holder.bodyTx.setText(list[position].body)
        holder.textView.setOnClickListener{
            val navController = Navigation.findNavController(ctx as Activity, R.id.sf)
                if (list[position].contains("https://api.github.com")){
                    val bundle:Bundle = Bundle()
                    bundle.putString("url",list[position])
                    navController.navigate(R.id.songDetailFragment,bundle)
                }else if(list[position].contains("http://m.qinyipu.com")){
                    val bundle:Bundle = Bundle()
                    bundle.putString("url",list[position])
                    navController.navigate(R.id.qinyipu,bundle)
                }
            /*  val intent:Intent=Intent()
              intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
              intent.setClass(ctx,SongDetailActivity::class.java)
              intent.putExtra("data",list[position])
              ctx.startActivity(intent)*/
            // 通过导航视图进行转移
         /*
           */
        }
    }

    class CollectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView:TextView = itemView.findViewById(R.id.rvi_title_tx)
        //  val bodyTx:TextView = itemView.findViewById(R.id.rvi_body_tx)
    }

}
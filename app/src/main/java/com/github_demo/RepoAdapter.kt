package com.github_demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github_demo.model.Repo


class RepoAdapter(private val viewHolderClicked: ViewHolderClicked<Repo>) : RecyclerView.Adapter<MyViewHolder>() {

    private val dataList: ArrayList<Repo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val parentView = LayoutInflater.from(parent.context).inflate(R.layout.repo_item_view, parent, false)
        return MyViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.view.findViewById<TextView>(R.id.txt_repoName)?.text = dataList[position].name
        holder.view.findViewById<TextView>(R.id.txt_repoDescription)?.text = dataList[position].description

        holder.view.setOnClickListener {
            viewHolderClicked.onItemViewClicked(holder.view, dataList[position])
        }
    }

    override fun getItemCount() = dataList.size

    fun setData(dataList: List<Repo>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)
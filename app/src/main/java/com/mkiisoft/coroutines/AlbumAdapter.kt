package com.mkiisoft.coroutines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class AlbumAdapter(var list: List<Album>? = null, private val click: (Album, List<Album>) -> Unit) : Adapter<AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder =
            AlbumViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false), list!!, click)

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount(): Int = list!!.size
}

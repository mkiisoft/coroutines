package com.mkiisoft.coroutines

import android.view.View
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_album.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumViewHolder(view: View, private val list: List<Album>, val click: (Album, List<Album>) -> Unit) : ViewHolder(view), View.OnClickListener {

    private lateinit var item: Album

    fun bind(album: Album) {
        item = album
        GlobalScope.launch(Dispatchers.IO) { loadImage(album.album) }
        itemView.setOnClickListener(this)
    }

    @WorkerThread
    suspend fun loadImage(image: String) {
        val bitmap = Glide.with(itemView.context).asBitmap().load(image).submit().get()
        withContext(Dispatchers.Main) { itemView.album_item.setImageBitmap(bitmap) }
    }

    override fun onClick(v: View?) {
        click(item, list)
    }
}
package com.example.github_connections.modules.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.github_connections.R
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

class FriendsRecyclerItem(
    data: Data
) : DiverseRecyclerAdapter.RecyclerItem<FriendsRecyclerItem.Data, FriendsRecyclerItem.ViewHolder>() {

    override val type: Int = TYPE
    override val data: Data? = data

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.friends_list, parent, false))

    data class Data(val imageUrl: String, val name: String)

    class ViewHolder(view: View) : DiverseRecyclerAdapter.ViewHolder<Data>(view) {

        private val friendsImage = findViewById<ImageView>(R.id.friends_image)!!
        private val friendName = findViewById<TextView>(R.id.friends_name)!!

        override fun bindTo(data: Data?) {

            if (data != null) {
                Glide.with(itemView.context).load(data.imageUrl)
                    .transform(RoundedCorners(115))
                    .into(friendsImage)

                friendName.text = data.name
            }
        }
    }

    companion object {
        const val TYPE = 3
    }
}
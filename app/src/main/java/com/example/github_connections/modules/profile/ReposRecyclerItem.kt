package com.example.github_connections.modules.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.github_connections.R
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

class ReposRecyclerItem(data: Data) :
    DiverseRecyclerAdapter.RecyclerItem<ReposRecyclerItem.Data, ReposRecyclerItem.ViewHolder>() {

    override val type: Int = TYPE
    override val data: Data? = data

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.repos_list, parent, false))

    //The data class for the info of each CachingDataSource in the list
    data class Data(
        val name: String,
        val description: String,
        val forks: String,
        val watchers: String
    )

    class ViewHolder(view: View) : DiverseRecyclerAdapter.ViewHolder<Data>(view) {

        private val repoName = findViewById<TextView>(R.id.repos_name)!!
        private val repoDescription = findViewById<TextView>(R.id.repos_description)!!
        private val repoForks = findViewById<TextView>(R.id.repos_forks)!!
        private val repoWatchers = findViewById<TextView>(R.id.repos_watchers)!!

        override fun bindTo(data: Data?) {

            if (data != null) {
                //Populating the textviews
                repoName.text = data.name
                repoDescription.text = data.description
                repoForks.text = data.forks
                repoWatchers.text = data.watchers
            }
        }
    }

    companion object {
        const val TYPE = 1
    }
}
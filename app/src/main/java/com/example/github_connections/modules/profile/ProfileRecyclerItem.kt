package com.example.github_connections.modules.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.github_connections.R
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter

class ProfileRecyclerItem(
    data: Data,
    private val context: Context,
    private val profileController: ProfileController
) : DiverseRecyclerAdapter.RecyclerItem<ProfileRecyclerItem.Data, ProfileRecyclerItem.ViewHolder>() {

    override val type: Int = TYPE
    override val data: Data? = data

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): ViewHolder =
        ViewHolder(inflater.inflate(R.layout.profile_details, parent, false), context, profileController)

    data class Data(
        val username: String,
        val pictureLink: String,
        val bio: String,
        val location: String,
        val followers: String,
        val following: String
    )

    class ViewHolder(view: View, private val context: Context, private val profileController: ProfileController) :
        DiverseRecyclerAdapter.ViewHolder<Data>(view) {

        private val profileUsername = findViewById<TextView>(R.id.profile_name)!!
        private val profilePicture = findViewById<ImageView>(R.id.profile_image)!!
        private val profileBio = findViewById<TextView>(R.id.profile_bio)!!
        private val profileLocation = findViewById<TextView>(R.id.profile_location)!!
        private val profileButtonFollowers = findViewById<Button>(R.id.profile_button_followers)!!
        private val profileButtonFollowing = findViewById<Button>(R.id.profile_button_following)!!

        override fun bindTo(data: Data?) {

            if (data != null) {
                Glide.with(context).load(data.pictureLink)
                    .transform(RoundedCorners(115))
                    .into(profilePicture)

                profileUsername.text = data.username
                profileBio.text = data.bio
                profileLocation.text = data.location
                profileBio.text = data.bio
                profileButtonFollowers.text = data.followers
                profileButtonFollowing.text = data.following
            }

            profileButtonFollowers.setOnClickListener { profileController.followersButtonClicked() }

            profileButtonFollowing.setOnClickListener { profileController.followingButtonClicked() }
        }
    }

    companion object {
        const val TYPE = 2
    }
}
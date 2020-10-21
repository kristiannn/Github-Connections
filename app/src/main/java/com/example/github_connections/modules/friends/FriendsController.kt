package com.example.github_connections.modules.friends

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.example.github_connections.BaseController
import com.example.github_connections.R
import com.example.github_connections.modules.dialog.DialogController
import com.example.github_connections.modules.profile.ProfileController
import com.example.github_connections.modules.util.EventObserver
import com.example.github_connections.modules.util.viewModel
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.layoutmanager.DiverseLinearLayoutManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class FriendsController(bundle: Bundle) : BaseController(bundle), KodeinAware {

    constructor(friendParcelable: ArrayList<FriendWrapper>, username: String) : this(Bundle().apply {
        putParcelableArrayList(FRIENDS_KEY, friendParcelable)
        putString(USERNAME_KEY, username)
    })

    private val friendsList by lazy { args.getParcelableArrayList<FriendWrapper>(FRIENDS_KEY)!! }

    private val initialUsername by lazy { args.getString(USERNAME_KEY)!! }

    override val kodein: Kodein = Kodein.lazy {
        extend((applicationContext as KodeinAware).kodein)
        import(friendsModule)
    }

    private val viewModel: FriendsViewModel by viewModel()

    private lateinit var friendsActivity: Activity
    private lateinit var vibrator: Vibrator
    private lateinit var logoutButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var friendsView: View
    private lateinit var friendsListRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val adapter: DiverseRecyclerAdapter = DiverseRecyclerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        friendsView = inflater.inflate(R.layout.friends, container, false)

        init()

        return friendsView
    }

    private fun init() {
        sendViewmodelSetupData()
        findViews()
        initAdapter()
        setObservers()
        setClickListeners()
    }

    private fun findViews() {
        friendsActivity = activity!!
        friendsListRecyclerView = friendsView.findViewById(R.id.friends_recyclerView)
        progressBar = friendsView.findViewById(R.id.progressBar)
        logoutButton = friendsView.findViewById(R.id.logoutButton)
        logoutButton.visibility = View.INVISIBLE
        backButton = friendsView.findViewById(R.id.backButton)
        vibrator = friendsActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private fun setClickListeners() {
        backButton.setOnClickListener { router.handleBack() }
    }

    private fun initAdapter() {
        friendsListRecyclerView.layoutManager = DiverseLinearLayoutManager(friendsActivity)
        friendsListRecyclerView.adapter = adapter
        adapter.removeAll()
    }

    private fun populateAdapter(adapter: DiverseRecyclerAdapter, friendList: List<FriendWrapper>) {
        adapter.removeAll()

        for ((index) in viewModel.friendsState.value?.friendList!!.withIndex()) {
            adapter.addItem(
                FriendsRecyclerItem(
                    FriendsRecyclerItem.Data(friendList[index].avatarUrl, friendList[index].name)
                ), false
            )
        }

        adapter.notifyDataSetChanged()

        adapter.onItemActionListener = object : DiverseRecyclerAdapter.OnItemActionListener() {
            override fun onItemClicked(v: View, position: Int) {
                val friendsListItem: FriendsRecyclerItem = adapter.getItem(position)
                viewModel.getProfile(friendsListItem.data?.name!!)
            }
        }
    }

    private fun sendViewmodelSetupData() {
        viewModel.initialViewmodelSetup(friendsList, initialUsername)
    }

    private fun setObservers() {
        viewModel.screenState.observe(
            this,
            Observer<FriendsViewModel.ScreenState> {
                if (it.isLoading) {
                    progressBar.visibility = View.VISIBLE

                    friendsActivity.window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )

                } else {
                    progressBar.visibility = View.INVISIBLE
                    friendsActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        )

        viewModel.friendsState.observe(
            this,
            Observer<FriendsViewModel.FriendsState> {
                if (viewModel.friendsState.value!!.profileName == initialUsername) {
                    when {
                        it.success -> {
                            populateAdapter(adapter, it.friendList)
                        }

                        it.error != null -> {
                            val error = it.error.message.toString()
                            val dialog = DialogController(error)
                            dialog.show((activity as AppCompatActivity).supportFragmentManager, "Error!")
                            vibrator.vibrate(100)
                        }
                    }
                }
            })

        viewModel.profileState.observe(
            this,
            EventObserver { event, content ->
                if (event is ProfileSuccess) {
                    router.pushController(
                        RouterTransaction.with(ProfileController(content as ProfileWrapper))
                            .pushChangeHandler(HorizontalChangeHandler())
                            .popChangeHandler(HorizontalChangeHandler())
                    )

                } else if (event is ErrorState) {
                    val error = content as Throwable
                    val dialog = DialogController(error.message!!)
                    dialog.show((activity as AppCompatActivity).supportFragmentManager, "Error!")
                    vibrator.vibrate(100)
                }
            })
    }

    companion object {
        private const val FRIENDS_KEY = "friends"
        private const val USERNAME_KEY = "user"
    }
}
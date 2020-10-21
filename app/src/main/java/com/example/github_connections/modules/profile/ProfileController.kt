package com.example.github_connections.modules.profile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
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
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.example.github_connections.BaseController
import com.example.github_connections.R
import com.example.github_connections.modules.dialog.DialogController
import com.example.github_connections.modules.friends.FriendsController
import com.example.github_connections.modules.login.LoginController
import com.example.github_connections.modules.util.EventObserver
import com.example.github_connections.modules.util.viewModel
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter
import com.trading212.diverserecycleradapter.layoutmanager.DiverseLinearLayoutManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class ProfileController(bundle: Bundle) : BaseController(bundle), KodeinAware {

    constructor(user: Parcelable) : this(Bundle().apply {
        putParcelable(USER_KEY, user)
    })

    override val kodein: Kodein = Kodein.lazy {
        extend((applicationContext as KodeinAware).kodein)
        import(profileModule)
    }

    private val profile by lazy { args.getParcelable<ProfileWrapper>(USER_KEY)!! }

    private val viewModel: ProfileViewModel by viewModel()

    private lateinit var profileActivity: Activity
    private lateinit var vibrator: Vibrator
    private lateinit var logoutButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var profileView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val adapter: DiverseRecyclerAdapter = DiverseRecyclerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        profileView = inflater.inflate(R.layout.profile, container, false)

        init()

        return profileView
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        viewModel.profile = profile
    }

    private fun init() {
        sendViewmodelSetupData()
        findViews()
        initAdapter()
        setObservers()
        setButtonClickListeners()
    }

    private fun findViews() {
        profileActivity = activity!!
        recyclerView = profileView.findViewById(R.id.profile_recyclerView_repos)
        progressBar = profileView.findViewById(R.id.progressBar)
        logoutButton = profileView.findViewById(R.id.logoutButton)
        backButton = profileView.findViewById(R.id.backButton)
        vibrator = profileActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private fun initAdapter() {
        recyclerView.layoutManager = DiverseLinearLayoutManager(profileActivity)
        recyclerView.adapter = adapter
    }

    private fun sendViewmodelSetupData() {
        viewModel.initialViewmodelSetup(profile)
    }

    private fun setObservers() {
        viewModel.screenState.observe(
            this,
            Observer<ProfileViewModel.ScreenState> {
                if (it.isLoading) {
                    progressBar.visibility = View.VISIBLE
                    profileActivity.window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                } else {
                    progressBar.visibility = View.INVISIBLE
                    profileActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        )

        viewModel.profileState.observe(
            this,
            Observer<ProfileViewModel.ProfileState> {
                if (viewModel.profile.name == profile.name) {
                    if (it.profileWrapper != null) {
                        populateProfileDetails(adapter, it.profileWrapper)
                    }

                    if (!it.repositoryList.isNullOrEmpty()) {
                        populateRepositoryDetails(adapter, it.repositoryList)
                    }

                    if (it.error != null) {
                        val error = it.error.message.toString()
                        val dialog = DialogController(error)
                        dialog.show((activity as AppCompatActivity).supportFragmentManager, "Error!")
                        vibrator.vibrate(100)
                    }
                }
            }
        )

        @Suppress("UNCHECKED_CAST")
        viewModel.friendsEvent.observe(
            this,
            EventObserver { event, content ->
                if (event is FriendsSuccess) {
                    router.pushController(
                        RouterTransaction.with(
                            FriendsController(
                                content as ArrayList<FriendWrapper>,
                                viewModel.profileState.value!!.profileWrapper!!.name
                            )
                        )
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

        viewModel.logoutState.observe(
            this,
            Observer<Boolean> {
                if (it) {
                    router.setRoot(
                        RouterTransaction.with(LoginController())
                            .pushChangeHandler(FadeChangeHandler())
                            .popChangeHandler(FadeChangeHandler())
                    )
                }
            }
        )
    }

    // We remove everything from the adapter here, since this method is the first one called and we would only
    // call it if we're reloading everything anyway..
    private fun populateProfileDetails(adapter: DiverseRecyclerAdapter, profileWrapper: ProfileWrapper) {
        adapter.addItem(
            ProfileRecyclerItem(
                ProfileRecyclerItem.Data(
                    profileWrapper.name,
                    profileWrapper.avatarUrl,
                    profileWrapper.bio,
                    profileWrapper.location,
                    resources!!.getString(R.string.profile_followers, profileWrapper.followersCount),
                    resources!!.getString(R.string.profile_following, profileWrapper.followingCount)
                )
                , profileActivity, this
            )
        )
    }

    private fun populateRepositoryDetails(adapter: DiverseRecyclerAdapter, repositoryList: List<RepositoryWrapper>) {
        for (index in repositoryList.indices) {
            adapter.addItem(
                ReposRecyclerItem(
                    ReposRecyclerItem.Data(
                        repositoryList[index].name,
                        repositoryList[index].description,
                        repositoryList[index].forksCount.toString(),
                        repositoryList[index].watchersCount.toString()
                    )
                )
                , false
            )
        }
        adapter.notifyDataSetChanged()
    }

    private fun setButtonClickListeners() {
        if (router.backstack.size > 1) {
            logoutButton.visibility = View.INVISIBLE

            backButton.visibility = View.VISIBLE
            backButton.setOnClickListener { router.handleBack() }

        } else {
            backButton.visibility = View.INVISIBLE
            logoutButton.visibility = View.VISIBLE

            logoutButton.setOnClickListener {
                viewModel.logoutUser()
            }
        }
    }

    //these methods are needed, because they connect the viewlisteners in the recycler item to actions in the viewmodel
    //As according to MVVM the only communication should be between the view itself and the viewmodel..
    fun followersButtonClicked() {
        viewModel.getFollowers()
    }

    fun followingButtonClicked() {
        viewModel.getFollowing()
    }

    companion object {
        private const val USER_KEY = "profileWrapper"
    }
}
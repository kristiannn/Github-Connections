package com.example.github_connections.modules.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_connections.Result
import com.example.github_connections.modules.util.AccountManager
import com.example.github_connections.modules.util.Event
import com.example.github_connections.repository.GithubRepository
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val githubRepository: GithubRepository,
    private val accountManager: AccountManager
) : ViewModel() {

    private val privateScreenState = MutableLiveData<ScreenState>()
    private val privateProfileState = MutableLiveData<ProfileState>()
    private val privateFriendsEvent = MutableLiveData<Event<*>>()
    private val privateLogoutState = MutableLiveData<Boolean>()

    val screenState: LiveData<ScreenState>
        get() = privateScreenState
    val profileState: LiveData<ProfileState>
        get() = privateProfileState
    val friendsEvent: LiveData<Event<*>>
        get() = privateFriendsEvent
    val logoutState: LiveData<Boolean>
        get() = privateLogoutState

    /* This is needed in its current form (outside of the observer objects) because it makes it easy to avoid
    * double loading. It's annoying to have the user details displayed first, then the screen to flash and repos to appear.
    * This is an easy way to avoid that plus deal with some other small issues that arise from removing it..*/
    lateinit var profile: ProfileWrapper

    private suspend fun setProfileData() {
        privateScreenState.value = ScreenState(isLoading = true)

        val repoResult = githubRepository.getRepos(profile.name)

        if (repoResult is Result.Success) {
            val repoSortedResults = repoResult.data.toMutableList()
            repoSortedResults.sortWith(compareBy({ it.forksCount }, { it.watchersCount }))
            repoSortedResults.reverse()

            privateProfileState.value =
                ProfileState(profileWrapper = profile, repositoryList = repoSortedResults)

            privateScreenState.value = ScreenState(isLoading = false)

        } else if (repoResult is Result.Error) {
            privateProfileState.value = ProfileState(error = repoResult.error)
            privateScreenState.value = ScreenState(isLoading = false)
        }
    }

    private suspend fun getFriends(followers: Boolean) {
        privateScreenState.value = ScreenState(isLoading = true)

        val friendResult: Result<List<FriendWrapper>> =
            if (followers) {

                if (profile.followersCount == 0) {
                    privateFriendsEvent.value = ErrorState(LoadingError.NoFriends)
                    privateScreenState.value = ScreenState(isLoading = false)
                    return
                }

                githubRepository.getFollowers(profile.name)
            } else {

                if (profile.followingCount == 0) {
                    privateFriendsEvent.value = ErrorState(LoadingError.NoFriends)
                    privateScreenState.value = ScreenState(isLoading = false)
                    return
                }

                githubRepository.getFollowing(profile.name)
            }

        if (friendResult is Result.Success) {

            if (friendResult.data.isEmpty()) {
                privateFriendsEvent.value = ErrorState(error = LoadingError.NothingFound)
            } else {
                privateFriendsEvent.value = FriendsSuccess(friendResult.data)
            }
            privateScreenState.value = ScreenState(isLoading = false)

        } else if (friendResult is Result.Error) {
            privateFriendsEvent.value = ErrorState(error = friendResult.error)
            privateScreenState.value = ScreenState(isLoading = false)
        }
    }

    fun logoutUser() {
        privateLogoutState.value = true
        accountManager.logoutUser()
    }

    fun initialViewmodelSetup(user: ProfileWrapper) {
        viewModelScope.launch {
            privateProfileState.value = ProfileState()
            privateLogoutState.value = false
            profile = user
            setProfileData()
        }
    }

    fun getFollowers() {
        viewModelScope.launch {
            getFriends(true)
        }
    }

    fun getFollowing() {
        viewModelScope.launch {
            getFriends(false)
        }
    }

    data class ProfileState(
        val profileWrapper: ProfileWrapper? = null,
        val repositoryList: List<RepositoryWrapper> = listOf(),
        val error: Throwable? = null
    )

    /*This makes a lot of sense to be a separate class, since FriendsEvent is not a state, but an Event.
     It would make in unnecessarily complicated to deal with loading in Events, which could easily lead to bugs
     from forgetting to set the loading to false. It being in the ScreenState and therefore ViewModel, makes it easier
     to deal with that, since we can include a check for that in the Unit tests, which we cannot do if it's dealt with in the Controller*/
    data class ScreenState(
        val isLoading: Boolean = false
    )
}

class FriendsSuccess(friendList: List<FriendWrapper>) : Event<List<FriendWrapper>>(friendList)
class ErrorState(error: Throwable) : Event<Throwable>(error)

sealed class LoadingError(message: String? = null) : Throwable(message) {
    object NothingFound : LoadingError("Error! Unable to find any users!")
    object NoFriends : LoadingError("This user does not have any friends!")
}
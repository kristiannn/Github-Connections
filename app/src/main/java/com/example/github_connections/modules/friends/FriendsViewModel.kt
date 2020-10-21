package com.example.github_connections.modules.friends

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
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val githubRepository: GithubRepository,
    private val accountManager: AccountManager
) : ViewModel() {

    private val privateFriendsState = MutableLiveData<FriendsState>()
    private val privateProfileState = MutableLiveData<Event<*>>()
    private val privateScreenState = MutableLiveData<ScreenState>()

    val friendsState: LiveData<FriendsState>
        get() = privateFriendsState
    val profileState: LiveData<Event<*>>
        get() = privateProfileState
    val screenState: MutableLiveData<ScreenState>
        get() = privateScreenState

    fun initialViewmodelSetup(listOfFriends: List<FriendWrapper>, username: String) {
        privateFriendsState.value = FriendsState(success = true, friendList = listOfFriends, profileName = username)
    }

    fun getProfile(username: String) {
        viewModelScope.launch {
            privateScreenState.value = ScreenState(isLoading = true)

            val result = githubRepository.getProfile(username)

            if (result is Result.Success) {
                privateProfileState.value = ProfileSuccess(user = result.data)
            } else if (result is Result.Error) {
                privateProfileState.value = ErrorState(error = result.error)
            }

            privateScreenState.value = ScreenState(isLoading = false)
        }
    }

    data class FriendsState(
        val friendList: List<FriendWrapper> = listOf(),
        val profileName: String = "",
        val success: Boolean = false,
        val error: Throwable? = null
    )

    data class ScreenState(
        val isLoading: Boolean = false
    )
}

class ProfileSuccess(user: ProfileWrapper) : Event<ProfileWrapper>(user)
class ErrorState(error: Throwable) : Event<Throwable>(error)
package com.example.github_connections.modules.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_connections.Result
import com.example.github_connections.modules.util.AccountManager
import com.example.github_connections.modules.util.Event
import com.example.github_connections.repository.GithubRepository
import com.example.github_connections.repository.models.ProfileWrapper
import kotlinx.coroutines.launch

class LoginViewModel(
    private val githubRepository: GithubRepository,
    private val accountManager: AccountManager
) : ViewModel() {

    private val privateLoginEvent = MutableLiveData<Event<*>>()
    private val privateScreenState = MutableLiveData<ScreenState>()
    private val privateProfileNameState = MutableLiveData<String>()

    val loginEvent: LiveData<Event<*>>
        get() = privateLoginEvent
    val screenState: LiveData<ScreenState>
        get() = privateScreenState
    val profileNameState: LiveData<String>
        get() = privateProfileNameState

    init {
        autoLogin()
    }

    fun login(username: String?) {
        privateScreenState.value = ScreenState(isLoading = true)

        if (username.isNullOrEmpty()) {
            privateScreenState.value = ScreenState(isLoading = false)
            privateLoginEvent.value = LoginError(ValidationError.EmptyUsername)

        } else {
            viewModelScope.launch {
                val result = githubRepository.getProfile(username)

                if (result is Result.Success) {
                    accountManager.saveUser(result.data.name)
                    privateProfileNameState.value = result.data.name
                    privateScreenState.value = ScreenState(isLoading = false)
                    privateLoginEvent.value = LoginSuccess(result.data)

                } else if (result is Result.Error) {
                    privateScreenState.value = ScreenState(isLoading = false)
                    privateLoginEvent.value = LoginError(result.error)
                }
            }
        }
    }

    private fun autoLogin() {
        if (accountManager.isUserLogged()) {
            privateProfileNameState.value = accountManager.getUser()
            login(privateProfileNameState.value)
        }
    }

    data class ScreenState(
        val isLoading: Boolean = false
    )
}

class LoginSuccess(user: ProfileWrapper) : Event<ProfileWrapper>(user)
class LoginError(error: Throwable) : Event<Throwable>(error)

sealed class ValidationError(message: String? = null) : Throwable(message) {
    object EmptyUsername : ValidationError("You need to type in a username!")
}
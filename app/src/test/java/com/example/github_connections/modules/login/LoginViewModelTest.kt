package com.example.github_connections.modules.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.github_connections.modules.testViewModels
import com.example.github_connections.modules.util.Event
import com.example.github_connections.modules.util.ViewModelFactory
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.testRepositoryModule
import com.example.github_connections.testUtilsModule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

@RunWith(AndroidJUnit4::class)
class LoginViewModelTest : KodeinAware {

    override val kodein: Kodein = Kodein {
        import(testRepositoryModule)
        import(testUtilsModule)
        import(testViewModels)
    }

    val loginViewModel = ViewModelFactory(kodein).create(LoginViewModel::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `login with valid username`() {
        val expectedProfile = ProfileWrapper(
            "Test Profile 1",
            "http://blaaaaaaaaaa.com",
            "Test Biography 1",
            "Test Location",
            1,
            1,
            1,
            "0"
        )

        val successfulUsername = "Test Profile 1"
        val listOfScreenStates = mutableListOf<LoginViewModel.ScreenState>()
        val listOfLoginEvents = mutableListOf<ProfileWrapper>()

        loginViewModel.loginEvent.observeForever { listOfLoginEvents.add(loginViewModel.loginEvent.value!!.getContent() as ProfileWrapper) }

        loginViewModel.screenState.observeForever { listOfScreenStates.add(loginViewModel.screenState.value!!) }

        loginViewModel.login(successfulUsername)

        assertEquals(expectedProfile, listOfLoginEvents.get(0))

        assertEquals(LoginViewModel.ScreenState(true), listOfScreenStates.get(0))
        assertEquals(LoginViewModel.ScreenState(false), listOfScreenStates.get(1))

        assertEquals(successfulUsername, listOfLoginEvents.get(0).name)
    }

    @Test
    fun `login with wrong username`() {
        val wrongUsername = "Fail Profile"

        val listOfScreenStates = mutableListOf<LoginViewModel.ScreenState>()
        val listOfLoginEvents = mutableListOf<Event<*>>()

        loginViewModel.loginEvent.observeForever { listOfLoginEvents.add(loginViewModel.loginEvent.value!!) }

        loginViewModel.screenState.observeForever { listOfScreenStates.add(loginViewModel.screenState.value!!) }

        loginViewModel.login(wrongUsername)

        assertTrue(listOfLoginEvents.get(0) is LoginError)

        assertEquals(LoginViewModel.ScreenState(true), listOfScreenStates.get(0))
        assertEquals(LoginViewModel.ScreenState(false), listOfScreenStates.get(1))
    }
}
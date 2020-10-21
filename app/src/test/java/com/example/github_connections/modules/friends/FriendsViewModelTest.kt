package com.example.github_connections.modules.friends

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.github_connections.modules.testViewModels
import com.example.github_connections.modules.util.Event
import com.example.github_connections.modules.util.ViewModelFactory
import com.example.github_connections.repository.models.FriendWrapper
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
class FriendsViewModelTest : KodeinAware {

    override val kodein: Kodein = Kodein {
        import(testRepositoryModule)
        import(testUtilsModule)
        import(testViewModels)
    }

    val friendsViewModel = ViewModelFactory(kodein).create(FriendsViewModel::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `put in data in the initialViewmodelSetup and check if everything is properly allocated`() {
        val expectedFriends: MutableList<FriendWrapper> = mutableListOf()
        expectedFriends.add(
            FriendWrapper(
                "Friend 1",
                "http://blablabla.com",
                "0"
            )
        )
        expectedFriends.add(
            FriendWrapper(
                "Friend 2",
                "http://blablabla.com",
                "0"
            )
        )
        expectedFriends.add(
            FriendWrapper(
                "Friend 3",
                "http://blablabla.com",
                "0"
            )
        )
        expectedFriends.add(
            FriendWrapper(
                "Friend 4",
                "http://blablabla.com",
                "0"
            )
        )

        val listOfFriendsStates = mutableListOf<FriendsViewModel.FriendsState>()

        friendsViewModel.friendsState.observeForever {
            listOfFriendsStates.add(friendsViewModel.friendsState.value!!)
        }

        friendsViewModel.initialViewmodelSetup(expectedFriends, "Test Profile 1")

        assertEquals(expectedFriends, listOfFriendsStates.get(0).friendList)
    }

    @Test
    fun `get profile successfully`() {
        val successfulUsername = "Test Profile 1"

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

        val listOfScreenStates = mutableListOf<FriendsViewModel.ScreenState>()
        val listOfProfileEvents = mutableListOf<Event<*>>()

        friendsViewModel.profileState.observeForever { listOfProfileEvents.add(friendsViewModel.profileState.value!!) }

        friendsViewModel.screenState.observeForever { listOfScreenStates.add(friendsViewModel.screenState.value!!) }

        friendsViewModel.getProfile(successfulUsername)

        assertTrue(listOfProfileEvents.get(0) is ProfileSuccess)

        assertEquals(expectedProfile, listOfProfileEvents.get(0).getContent())

        assertEquals(FriendsViewModel.ScreenState(true), listOfScreenStates.get(0))
        assertEquals(FriendsViewModel.ScreenState(false), listOfScreenStates.get(1))

    }

    @Test
    fun `get profile unsuccessfully`() {
        val wrongUsername = "Fail Profile"

        val listOfScreenStates = mutableListOf<FriendsViewModel.ScreenState>()
        val listOfProfileEvents = mutableListOf<Event<*>>()

        friendsViewModel.profileState.observeForever {
            listOfProfileEvents.add(friendsViewModel.profileState.value!!)
        }

        friendsViewModel.screenState.observeForever {
            listOfScreenStates.add(friendsViewModel.screenState.value!!)
        }

        friendsViewModel.getProfile(wrongUsername)

        assertTrue(listOfProfileEvents.get(0) is ErrorState)

        assertEquals(FriendsViewModel.ScreenState(true), listOfScreenStates.get(0))
        assertEquals(FriendsViewModel.ScreenState(false), listOfScreenStates.get(1))
    }
}
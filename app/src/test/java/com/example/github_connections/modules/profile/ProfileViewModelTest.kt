package com.example.github_connections.modules.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.github_connections.modules.testViewModels
import com.example.github_connections.modules.util.Event
import com.example.github_connections.modules.util.ViewModelFactory
import com.example.github_connections.repository.models.FriendWrapper
import com.example.github_connections.repository.models.ProfileWrapper
import com.example.github_connections.repository.models.RepositoryWrapper
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
class ProfileViewModelTest : KodeinAware {

    override val kodein: Kodein = Kodein {
        import(testRepositoryModule)
        import(testUtilsModule)
        import(testViewModels)
    }

    val profileViewModel = ViewModelFactory(kodein).create(ProfileViewModel::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `test if initialViewmodelSetup is populating everything properly`() {
        val profileSuccess = ProfileWrapper(
            "Test Profile 1",
            "http://blaaaaaaaaaa.com",
            "Test Biography 1",
            "Test Location",
            1,
            1,
            1,
            "0"
        )

        //These are manually ordered in ordrer to test if the viewmodel will order them properly..
        val expectedRepositories: MutableList<RepositoryWrapper> = mutableListOf()
        expectedRepositories.add(
            RepositoryWrapper(
                "Fake Rep 3",
                "Fake Description 3",
                2,
                7,
                "0"
            )
        )
        expectedRepositories.add(
            RepositoryWrapper(
                "Fake Rep 1",
                "Fake Description 1",
                4,
                5,
                "0"
            )
        )
        expectedRepositories.add(
            RepositoryWrapper(
                "Fake Rep 2",
                "Fake Description 2",
                6,
                4,
                "0"
            )
        )
        expectedRepositories.add(
            RepositoryWrapper(
                "Fake Rep 4",
                "Fake Description 4",
                3,
                3,
                "0"
            )
        )

        val listOfScreenStates = mutableListOf<ProfileViewModel.ScreenState>()
        val listOfProfileStates = mutableListOf<ProfileViewModel.ProfileState>()

        profileViewModel.profileState.observeForever {
            listOfProfileStates.add(profileViewModel.profileState.value!!)
        }

        profileViewModel.screenState.observeForever {
            listOfScreenStates.add(profileViewModel.screenState.value!!)
        }

        profileViewModel.initialViewmodelSetup(profileSuccess)

        assertEquals(profileSuccess, listOfProfileStates.get(1).profileWrapper)
        assertEquals(expectedRepositories, listOfProfileStates.get(1).repositoryList)

        assertEquals(ProfileViewModel.ScreenState(true), listOfScreenStates.get(0))
        assertEquals(ProfileViewModel.ScreenState(false), listOfScreenStates.get(1))
    }

    @Test
    fun `gather following then followers successfully`() {
        val profile = ProfileWrapper(
            "Test Profile 1",
            "http://blaaaaaaaaaa.com",
            "Test Biography 1",
            "Test Location",
            1,
            1,
            1,
            "0"
        )

        val expectedFriends = mutableListOf<FriendWrapper>()
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

        val listOfScreenStates = mutableListOf<ProfileViewModel.ScreenState>()
        val listOfFriendsEvents = mutableListOf<Event<*>>()

        profileViewModel.friendsEvent.observeForever {
            listOfFriendsEvents.add(profileViewModel.friendsEvent.value!!)
        }

        profileViewModel.screenState.observeForever {
            listOfScreenStates.add(profileViewModel.screenState.value!!)
        }

        profileViewModel.initialViewmodelSetup(profile)
        profileViewModel.getFollowing()

        val followingList = listOfFriendsEvents.get(0).getContent()

        assertTrue(listOfFriendsEvents.get(0) is FriendsSuccess)
        assertEquals(followingList, expectedFriends)

        assertEquals(ProfileViewModel.ScreenState(true), listOfScreenStates.get(0))
        assertEquals(ProfileViewModel.ScreenState(false), listOfScreenStates.get(1))

        profileViewModel.getFollowers()

        val followersList = listOfFriendsEvents.get(1).getContent()

        assertTrue(listOfFriendsEvents.get(1) is FriendsSuccess)
        assertEquals(followersList, expectedFriends)

        assertEquals(ProfileViewModel.ScreenState(true), listOfScreenStates.get(2))
        assertEquals(ProfileViewModel.ScreenState(false), listOfScreenStates.get(3))
    }

    @Test
    fun `gather friends of user with no friends`() {
        val profileFail = ProfileWrapper(
            "Test Profile 1",
            "http://blaaaaaaaaaa.com",
            "Test Biography 1",
            "Test Location",
            0,
            0,
            0,
            "0"
        )

        val listOfScreenStates = mutableListOf<ProfileViewModel.ScreenState>()
        val listOfFriendsEvents = mutableListOf<Event<*>>()

        profileViewModel.friendsEvent.observeForever {
            listOfFriendsEvents.add(profileViewModel.friendsEvent.value!!)
        }

        profileViewModel.screenState.observeForever {
            listOfScreenStates.add(profileViewModel.screenState.value!!)
        }

        profileViewModel.initialViewmodelSetup(profileFail)

        profileViewModel.getFollowing()
        val error = listOfFriendsEvents.get(0).getContent() as Throwable

        assertTrue(listOfFriendsEvents.get(0) is ErrorState)
        assertEquals(error, LoadingError.NoFriends)

        assertEquals(ProfileViewModel.ScreenState(true), listOfScreenStates.get(0))
        assertEquals(ProfileViewModel.ScreenState(false), listOfScreenStates.get(1))
    }

    @Test
    fun `logout user`() {
        profileViewModel.logoutUser()

        assertTrue(profileViewModel.logoutState.value!!)
    }
}
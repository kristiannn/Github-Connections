package com.example.github_connections

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.github_connections.modules.friends.FriendsRecyclerItem
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    /* Currently tests have a set-up issue. For some reason, they don't work with internet on,
    *  most likely because of loading times. Also, tests currently expect you to be logged in
    *  with user "hamidr" and have previously loaded all the data we're about to use in these tests,
    *  as we're without an internet connection - we'd get dialogs saying data can't be loaded if it's not
    *  been stored previously.. */

    @Rule
    @JvmField
    val rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        //Have to find a way to delog users here..
    }

    //@Test
    fun login() {
        isDisplayed().matches(R.id.login_layout)
        //onView(withId(R.id.login_editbox_username)).perform(typeText("Trading212"), pressImeActionButton())
        //onView(withId(R.id.login_button_login)).perform(click())
        isDisplayed().matches(R.id.profile_layout)
    }

    @Test
    fun login_and_navigate_to_friends() {
        //onView(withId(R.id.login_editbox_username)).perform(typeText("hamidr"), closeSoftKeyboard())
        //onView(withId(R.id.login_button_login)).perform(click())
        isDisplayed().matches(R.id.login_layout)
        isDisplayed().matches(R.id.profile_layout)

        onView(withId(R.id.profile_button_followers)).perform(click())
        isDisplayed().matches(R.id.friends_layout)
    }

    @Test
    fun login_navigate_to_friends_click_on_friend() {
        //onView(withId(R.id.login_editbox_username)).perform(typeText("hamidr"), closeSoftKeyboard())
        //onView(withId(R.id.login_button_login)).perform(click())
        isDisplayed().matches(R.id.login_layout)
        isDisplayed().matches(R.id.profile_layout)

        onView(withId(R.id.profile_button_followers)).perform(click())
        isDisplayed().matches(R.id.friends_layout)

        onView(withId(R.id.friends_recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FriendsRecyclerItem.ViewHolder>(
                0,
                click()
            )
        )

        isDisplayed().matches(R.id.profile_layout)
    }

    @Test
    fun login_navigate_to_friends_scroll_and_click_on_friend() {
        //onView(withId(R.id.login_editbox_username)).perform(typeText("hamidr"), closeSoftKeyboard())
        //onView(withId(R.id.login_button_login)).perform(click())
        isDisplayed().matches(R.id.login_layout)
        isDisplayed().matches(R.id.profile_layout)

        onView(withId(R.id.profile_button_followers)).perform(click())
        isDisplayed().matches(R.id.friends_layout)

        onView(withId(R.id.friends_recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<FriendsRecyclerItem.ViewHolder>(hasDescendant(withText("qa1")))
            )

        onView(withId(R.id.friends_recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FriendsRecyclerItem.ViewHolder>(
                23,
                click()
            )
        )

        isDisplayed().matches(R.id.profile_layout)
    }

    @Test
    fun log_out_and_in_again_with_login_button() {
        isDisplayed().matches(R.id.login_layout)
        isDisplayed().matches(R.id.profile_layout)

        onView(withId(R.id.logoutButton)).perform(click())
        isDisplayed().matches(R.id.login_layout)

        onView(withId(R.id.login_editbox_username)).perform(clearText(), typeText("hamidr"), closeSoftKeyboard())
        onView(withId(R.id.login_button_login)).perform(click())
        isDisplayed().matches(R.id.profile_layout)
    }

    @Test
    fun log_out_and_in_again_with_ime_action() {
        isDisplayed().matches(R.id.login_layout)
        isDisplayed().matches(R.id.profile_layout)

        onView(withId(R.id.logoutButton)).perform(click())
        isDisplayed().matches(R.id.login_layout)

        onView(withId(R.id.login_editbox_username)).perform(clearText(), typeText("hamidr"), pressImeActionButton())
        isDisplayed().matches(R.id.profile_layout)
    }

    @Test
    fun go_deep_and_go_back() {
        isDisplayed().matches(R.id.login_layout)
        isDisplayed().matches(R.id.profile_layout)


        onView(withId(R.id.profile_button_followers)).perform(click())

        isDisplayed().matches(R.id.friends_layout)

        onView(withId(R.id.friends_recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FriendsRecyclerItem.ViewHolder>(
                0,
                click()
            )
        )

        isDisplayed().matches(R.id.profile_layout)

        onView(withId(R.id.profile_button_following)).perform(click())
        isDisplayed().matches(R.id.friends_layout)

        onView(withId(R.id.backButton)).perform(click())
        isDisplayed().matches(R.id.profile_layout)

        onView(withId(R.id.backButton)).perform(click())
        isDisplayed().matches(R.id.friends_layout)

        onView(withId(R.id.backButton)).perform(click())
        isDisplayed().matches(R.id.profile_layout)
    }
}

package com.example.github_connections.modules.login

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.example.github_connections.BaseController
import com.example.github_connections.R
import com.example.github_connections.modules.dialog.DialogController
import com.example.github_connections.modules.profile.ProfileController
import com.example.github_connections.modules.util.EventObserver
import com.example.github_connections.modules.util.viewModel
import com.example.github_connections.repository.models.ProfileWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware


class LoginController : BaseController(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        extend((applicationContext as KodeinAware).kodein)
        import(loginModule)
    }

    private val viewModel: LoginViewModel by viewModel()

    private lateinit var loginActivity: Activity
    private lateinit var vibrator: Vibrator
    private lateinit var loginView: View
    private lateinit var loginButton: Button
    private lateinit var usernameText: EditText
    private lateinit var loginDescription: TextView
    private lateinit var usernameLogo: ImageView
    private lateinit var appLogo: ImageView
    private lateinit var appName: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var imm: InputMethodManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        loginView = inflater.inflate(R.layout.login, container, false)

        init()

        return loginView
    }

    private fun init() {
        findViews()
        viewSetup()
        setObservers()
        animateViews()
    }

    private fun findViews() {
        loginActivity = activity!!
        loginButton = loginView.findViewById(R.id.login_button_login)
        usernameText = loginView.findViewById(R.id.login_editbox_username)
        progressBar = loginView.findViewById(R.id.progressBar)
        loginDescription = loginView.findViewById(R.id.login_description)
        usernameLogo = loginView.findViewById(R.id.login_user_logo)
        appLogo = loginView.findViewById(R.id.login_git_logo)
        appName = loginView.findViewById(R.id.login_title)
        vibrator = loginActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        imm = loginActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun viewSetup() {
        loginButton.setOnClickListener {
            onLoginClick()
        }

        usernameText.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    onLoginClick()
                    true
                }
                else -> false
            }
        }
    }

    private fun setObservers() {
        viewModel.loginEvent.observe(
            this,
            EventObserver { event, content ->
                if (event is LoginSuccess) {
                    router.setRoot(
                        RouterTransaction.with(ProfileController(content as ProfileWrapper))
                            .pushChangeHandler(HorizontalChangeHandler())
                            .popChangeHandler(HorizontalChangeHandler())
                    )
                } else if (event is LoginError) {
                    val error = content as Throwable
                    val dialog = DialogController(error.message!!)
                    dialog.show((activity as AppCompatActivity).supportFragmentManager, "Error!")
                    vibrator.vibrate(100)
                }
            }
        )

        viewModel.screenState.observe(
            this,
            Observer<LoginViewModel.ScreenState> {
                progressBar.visibility = if (it.isLoading) View.VISIBLE else View.INVISIBLE
            }
        )

        viewModel.profileNameState.observe(
            this,
            Observer<String> {
                usernameText.setText(viewModel.profileNameState.value)
            }
        )
    }

    private fun animateViews() {

        ObjectAnimator.ofFloat(appLogo, View.TRANSLATION_Y, dpToPx(100), appLogo.y)
            .setDuration(1000)
            .start()

        ObjectAnimator.ofFloat(appName, View.TRANSLATION_Y, dpToPx(100), appName.y)
            .setDuration(1000)
            .start()

        ObjectAnimator.ofFloat(loginButton, View.TRANSLATION_Y, dpToPx(190), loginButton.y)
            .setDuration(1000)
            .start()

        ObjectAnimator.ofFloat(usernameText, View.TRANSLATION_Y, dpToPx(190), usernameText.y)
            .setDuration(1000)
            .start()

        ObjectAnimator.ofFloat(usernameLogo, View.TRANSLATION_Y, dpToPx(190), usernameLogo.y)
            .setDuration(1000)
            .start()

        ObjectAnimator.ofFloat(loginDescription, View.TRANSLATION_Y, dpToPx(190), loginDescription.y)
            .setDuration(1000)
            .start()
    }

    private fun dpToPx(dp: Int): Float {
        return dp * Resources.getSystem().getDisplayMetrics().density
    }

    private fun onLoginClick() {
        imm.hideSoftInputFromWindow(usernameText.windowToken, 0)
        viewModel.login(usernameText.text.toString())
    }
}
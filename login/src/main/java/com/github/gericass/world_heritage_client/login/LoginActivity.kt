package com.github.gericass.world_heritage_client.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private val navigator: AvgleNavigator.LoginNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_login)
        // Choose authentication providers
        FirebaseAuth.getInstance().currentUser?.let {
            navigator.run { navigateToHome() }
            return
        }
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                FirebaseAuth.getInstance().currentUser
                navigator.run { navigateToHome() }
            } else {
                Timber.e(response?.error)
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1
    }
}

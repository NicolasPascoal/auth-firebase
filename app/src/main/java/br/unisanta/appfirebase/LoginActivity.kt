package br.unisanta.appfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> onSignInResult(result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            openHome()
        } else if (savedInstanceState == null) {
            launchSignIn()
        }
    }

    private fun launchSignIn() {
        // O provedor de e-mail do FirebaseUI já inclui o fluxo de cadastro de novos usuários.
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setAlwaysShowSignInMethodScreen(true)
            .setLogo(R.drawable.ic_logo)
            .setTheme(R.style.Theme_LoginFirebase_Auth)
            .build()
        signInLauncher.launch(intent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        when {
            result.resultCode == RESULT_OK -> openHome()
            result.idpResponse == null -> finish() // usuário voltou sem entrar
            else -> {
                Toast.makeText(this, R.string.error_sign_in, Toast.LENGTH_LONG).show()
                launchSignIn()
            }
        }
    }

    private fun openHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}

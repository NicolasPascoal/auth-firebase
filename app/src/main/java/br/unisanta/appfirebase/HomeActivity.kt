package br.unisanta.appfirebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.unisanta.appfirebase.databinding.ActivityHomeBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            openLogin()
            return
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = user.displayName?.takeIf { it.isNotBlank() } ?: user.email.orEmpty()
        binding.textWelcome.text = getString(R.string.home_welcome, name)
        binding.textEmail.text = user.email
        binding.buttonSignOut.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener { openLogin() }
        }
    }

    private fun openLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }
}

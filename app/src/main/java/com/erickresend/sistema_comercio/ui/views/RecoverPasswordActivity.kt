package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.databinding.ActivityRecoverPasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRecoverPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun onStart() {
        super.onStart()

        _binding.btnChange.setOnClickListener {

            val email = _binding.editEmail.text.toString()

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snackbarSuccess = Snackbar.make(it,
                            getString(R.string.text_recover_password_snackbar), Snackbar.LENGTH_SHORT)
                        snackbarSuccess.setBackgroundTint(Color.BLUE)
                        snackbarSuccess.show()
                    }
                }
        }
    }
}
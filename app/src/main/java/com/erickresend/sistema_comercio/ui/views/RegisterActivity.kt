package com.erickresend.sistema_comercio.ui.views

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btnRegister.setOnClickListener { view ->

            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            hideKeybord()

            if(email.isEmpty() || password.isEmpty()) {
                val snackbar = Snackbar.make(view, getString(R.string.empty_fields), Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { register ->
                        if(register.isSuccessful) {
                            Toast.makeText(this, getString(R.string.success_register_user), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    }.addOnFailureListener { exception ->
                        val mensagemErro = when(exception) {
                            is FirebaseAuthWeakPasswordException -> getString(R.string.minimum_password)
                            is FirebaseAuthInvalidCredentialsException -> getString(R.string.input_valid_email)
                            is FirebaseAuthUserCollisionException -> getString(R.string.account_already_registered)
                            is FirebaseNetworkException -> getString(R.string.no_internet_connection)
                            else -> getString(R.string.error_register_user)
                        }
                        val snackbarError = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbarError.setBackgroundTint(Color.RED)
                        snackbarError.show()
                    }
            }
        }

        binding.btnShowPassword.setOnClickListener {

            val teste = binding.editPassword.inputType

            if (teste == 129) {
                binding.btnShowPassword.setImageResource(R.drawable.img_eye_open)
                binding.editPassword.inputType = 1
            } else {
                binding.btnShowPassword.setImageResource(R.drawable.img_eye_close)
                binding.editPassword.inputType = 129
            }
        }
    }

    private fun hideKeybord() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}
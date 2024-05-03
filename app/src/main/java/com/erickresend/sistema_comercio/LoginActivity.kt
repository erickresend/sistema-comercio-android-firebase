package com.erickresend.sistema_comercio

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.erickresend.sistema_comercio.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btnLogin.setOnClickListener { view ->

            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                val snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { register ->
                        if(register.isSuccessful) {
                            startActivity(Intent(this, ProductsActivity::class.java))
                        }
                    }.addOnFailureListener { exception ->
                        val mensagemErro = when(exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                            is FirebaseNetworkException -> "Sem conexão com a internet!"
                            else -> "Erro ao cadastrar usuário!"
                        }
                        val snackbarError = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbarError.setBackgroundTint(Color.RED)
                        snackbarError.show()
                    }
            }
        }

        binding.textNoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

            if(email.isEmpty() || password.isEmpty()) {
                val snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { register ->
                    if(register.isSuccessful) {
                        val snackbarSuccess = Snackbar.make(view, "Sucesso ao cadastrar usuário!", Snackbar.LENGTH_SHORT)
                        snackbarSuccess.setBackgroundTint(Color.BLUE)
                        snackbarSuccess.show()
                        binding.editEmail.setText("")
                        binding.editPassword.setText("")
                    }
                }.addOnFailureListener { exception ->
                        val mensagemErro = when(exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                            is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada!"
                            is FirebaseNetworkException -> "Sem conexão com a internet!"
                            else -> "Erro ao cadastrar usuário!"
                        }
                        val snackbarError = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbarError.setBackgroundTint(Color.RED)
                        snackbarError.show()
                }
            }
        }
    }
}
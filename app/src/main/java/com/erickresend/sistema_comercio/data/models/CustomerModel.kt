package com.erickresend.sistema_comercio.data.models

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class CustomerModel(

    @DocumentId
    val documentId: String? = null,

    val name: String? = null,

    val cpf: String? = null,

    val phone: String? = null,

    val birthDate: String? = null,

    val address: String? = null
): Serializable

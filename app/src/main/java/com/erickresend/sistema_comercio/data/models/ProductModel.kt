package com.erickresend.sistema_comercio.data.models

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class ProductModel(

    @DocumentId
    val documentId: String? = null,

    val name: String? = null,

    val price: Double? = null
): Serializable

package com.erickresend.sistema_comercio.data.models

import com.google.firebase.firestore.DocumentId

data class ProductModel(

    @DocumentId
    val documentId: String? = null,

    val name: String? = null,

    val price: Double? = null
)

package com.erickresend.sistema_comercio.data.models

import com.google.firebase.firestore.DocumentId

data class PointOfSaleModel(

    @DocumentId
    val documentId: String? = null,

    val change: Int = 0,

    val totalSaleDay: Double = 0.0
)

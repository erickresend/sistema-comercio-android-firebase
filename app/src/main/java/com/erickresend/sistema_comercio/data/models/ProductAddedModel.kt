package com.erickresend.sistema_comercio.data.models

data class ProductAddedModel(

    val productId: String? = null,

    val name: String? = null,

    val price: Double? = null,

    val discount: Double = 0.0
)

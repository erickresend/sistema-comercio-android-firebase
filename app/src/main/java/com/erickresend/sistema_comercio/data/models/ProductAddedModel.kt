package com.erickresend.sistema_comercio.data.models

data class ProductAddedModel(

    val name: String? = null,

    val price: Double? = null,

    val quantity: Int = 1,

    val discount: Double = 0.0
)

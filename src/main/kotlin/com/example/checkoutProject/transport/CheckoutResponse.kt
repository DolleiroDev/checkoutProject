package com.example.checkoutProject.transport

data class CheckoutResponse(
    val totalValue: Int,
    val productsRequest: List<ProductRequest>,
)

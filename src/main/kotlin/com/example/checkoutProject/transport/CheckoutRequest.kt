package com.example.checkoutProject.transport


data class CheckoutRequest(
    val products: List<ProductRequest>,
)

data class ProductRequest(
    val id: Long,
    val quantity: Int,
    val is_gift: Boolean,
    val unitValue: Int,
    val totalValue: Int,
)
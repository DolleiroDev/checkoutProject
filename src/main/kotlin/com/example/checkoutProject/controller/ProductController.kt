package com.example.checkoutProject.controller

import com.example.checkoutProject.repositories.ProductRepository
import com.example.checkoutProject.transport.CheckoutRequest
import com.example.checkoutProject.transport.CheckoutResponse
import com.example.checkoutProject.transport.ProductRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("products")

class ProductController(private val productRepository: ProductRepository){

    @PostMapping("/checkout")
    fun checkout(@RequestBody checkoutRequest: CheckoutRequest): CheckoutResponse {

        val products = mutableListOf<ProductRequest>()
        checkoutRequest.products.forEach {
            val product = (productRepository.findById(it.id))
            if (product != null) {
                val newProductRequest = it.copy(unitValue = product.value, totalValue = product.value * it.quantity)
                products.add(newProductRequest)
            }
        }
        val totalValue = products.sumOf { it.totalValue }
        val productsTransport = productRepository.checkIsBlackFriday(products)
        return CheckoutResponse(totalValue = totalValue, productsRequest = productsTransport)

    }
}
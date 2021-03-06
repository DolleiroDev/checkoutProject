package com.example.checkoutProject.repositories

import com.example.checkoutProject.models.Product
import com.example.checkoutProject.transport.ProductRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.stereotype.Repository
import java.io.File
import java.time.LocalDate
import java.time.Month

@Repository
class ProductRepository {
    private val jsonString: String = File("C:\\Users\\z003fydd\\Downloads\\ceepws\\checkoutProject\\src\\main\\kotlin\\products.json").readText(Charsets.UTF_8)
    private val gson = Gson()
    private val listProductType = object : TypeToken<List<Product>>(){}.type
    private val products: List<Product> = gson.fromJson(jsonString, listProductType)

    fun findById(id: Long): Product? {
        return products.find { it.id == id }
    }
    fun checkIsBlackFriday(products: MutableList<ProductRequest>): MutableList<ProductRequest> {
        val actualDate = LocalDate.now()
        val blackFridayDate = LocalDate.of(2021, Month.SEPTEMBER, 27)
        val randomProduct : ProductRequest
        val freeProduct : ProductRequest
        if (actualDate == blackFridayDate) {
            randomProduct = products.random()
            if (randomProduct.quantity > 1) {
                val payingProduct = randomProduct.copy(quantity = randomProduct.quantity - 1, totalValue = randomProduct.unitValue * (randomProduct.quantity - 1))
                freeProduct = payingProduct.copy(quantity = 1, totalValue = 0, unitValue =  0, is_gift = true)
                products.removeIf {
                    payingProduct.id == it.id
                }
                products.add(payingProduct)
            } else {
                freeProduct = randomProduct.copy(totalValue = 0, unitValue = 0, is_gift = true)
                products.removeIf {
                    freeProduct.id == it.id
                }
            }

            products.add(freeProduct)
        }
        return products
    }
}
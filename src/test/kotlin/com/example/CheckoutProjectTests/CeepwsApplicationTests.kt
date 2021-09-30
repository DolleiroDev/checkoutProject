package com.example.checkoutProjectTests

import com.example.checkoutProject.models.Product
import com.example.checkoutProject.repositories.ProductRepository
import com.example.checkoutProject.transport.CheckoutRequest
import com.example.checkoutProject.transport.ProductRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class CeepwsApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired
	lateinit var productRepository: ProductRepository

	@Test
	fun testFindById() {
		val products = listOf(
			Product(id = 1, title = "Ergonomic Wooden Pants", value = 15157),
			Product(id = 2, title = " Wooden Pants", value = 21212),
		)
		val findById = productRepository.findById(50L)!!
		Assertions.assertEquals(findById, products[0])
	}
	@Test
	fun testCheckout() {
		val productsRequest = listOf(
			ProductRequest(id = 1, quantity = 2, is_gift = false, unitValue = 124141, totalValue = 21131),
			ProductRequest(id = 2, quantity = 1, is_gift = false, unitValue = 312312, totalValue = 414123)
		)
		val content = ObjectMapper().writeValueAsString(CheckoutRequest(products = productsRequest))
		mockMvc.perform(MockMvcRequestBuilders.post("/products/checkout")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andDo(MockMvcResultHandlers.print())
	}
	@Test
	fun testCheckIsBlackFriday() {
		val productsRequest = mutableListOf(
			ProductRequest(id = 1, quantity = 1, is_gift = false, unitValue = 124141, 1213125),
			ProductRequest(id = 2, quantity = 1, is_gift = false, unitValue = 312312, 2312312)
		)
		val giftedProduct = ProductRequest(id = 1, quantity = 1, is_gift = true, unitValue = 0, totalValue = 0)
		val productsTransport = productRepository.checkIsBlackFriday(productsRequest)
		Assertions.assertEquals(productsTransport[1],giftedProduct)
	}
}

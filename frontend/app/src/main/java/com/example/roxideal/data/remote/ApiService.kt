package com.example.roxideal.data.remote

import java.math.BigDecimal
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val password: String
)

data class TokenResponse(val token: String)

data class CategoryDto(val id: Long, val name: String)

data class ProductDto(
    val id: Long,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val stock: Int,
    val imageUrl: String?,
    val unit: String?,
    val category: CategoryDto?
)

data class ProductRequest(
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val stock: Int,
    val imageUrl: String?
)

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): TokenResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @GET("api/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Long): ProductDto

    @POST("api/products")
    suspend fun createProduct(@Body request: ProductRequest): ProductDto

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body request: ProductRequest): ProductDto

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long)
}

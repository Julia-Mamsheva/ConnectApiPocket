package com.example.connectapisupabase.data.api

import com.example.connectapisupabase.data.model.BooksItems
import com.example.connectapisupabase.data.model.OtpResponses
import com.example.connectapisupabase.data.model.ResponsesAuth
import com.example.connectapisupabase.data.model.UserResponse
import com.example.connectapisupabase.domain.model.OTPAuth
import com.example.connectapisupabase.domain.model.OtpRequest
import com.example.connectapisupabase.domain.model.UserAuth
import com.example.connectapisupabase.domain.model.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


/**
 * Интерфейс, который определяет методы для выполнения HTTP-запросов к API
 * */
interface ApiConnect {
    // Метод для получения списка книг
    // Использует HTTP GET запрос по указанному пути
    // Принимает заголовок "Authorization" для аутентификации
    @GET("/api/collections/books/records")
    fun getBooks(@Header("Authorization") token: String): Call<BooksItems>

    // Метод для регистрации нового пользователя
    // Использует HTTP POST запрос по указанному пути
    // Принимает объект UserRequest в теле запроса
    @POST("/api/collections/users/records")
    fun signUp(@Body request: UserRequest): Call<UserResponse>

    // Метод для аутентификации пользователя по паролю
    // Использует HTTP POST запрос по указанному пути
    // Принимает объект UserAuth в теле запроса
    @POST("/api/collections/users/auth-with-password")
    fun signIn(@Body request: UserAuth): Call<ResponsesAuth>

    // Метод для аутентификации пользователя по OTP
    // Использует HTTP POST запрос по указанному пути
    // Принимает объект UserAuth в теле запроса
    @POST("/api/collections/users/request-otp")
    fun getOTP(@Body request: OtpRequest): Call<OtpResponses>

    // Метод для аутентификации пользователя по OTP т одноразовому паролю
    // Использует HTTP POST запрос по указанному пути
    // Принимает объект UserAuth в теле запроса
    @POST("/api/collections/users/auth-with-otp")
    fun signInWithOTP(@Body request: OTPAuth): Call<ResponsesAuth>
}
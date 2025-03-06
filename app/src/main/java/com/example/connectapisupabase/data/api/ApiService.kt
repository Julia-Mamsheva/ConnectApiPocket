package com.example.connectapisupabase.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    // Константа, содержащая базовый URL для API.
    // 10.0.2.2 используется для доступа к локальному серверу из эмулятора Android
    private const val BASE_URL = "http://10.0.2.2:8090"

    // Ленивая инициализация экземпляра ApiConnect
    // Это означает, что объект будет создан только при первом обращении к нему
    val instance: ApiConnect by lazy {
        // Создание экземпляра Retrofit с указанным базовым URL и конвертером JSON
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // Установка базового URL для запросов
            .addConverterFactory(GsonConverterFactory.create()) // Добавление конвертера для преобразования JSON в объекты Kotlin
            .build() // Построение экземпляра Retrofit.

        // Создание реализации интерфейса ApiConnect, который будет использоваться для выполнения API-запросов.
        retrofit.create(ApiConnect::class.java)
    }
}
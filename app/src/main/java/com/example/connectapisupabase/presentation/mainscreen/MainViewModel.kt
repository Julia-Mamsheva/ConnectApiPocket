package com.example.connectapisupabase.presentation.mainscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connectapisupabase.data.api.ApiService
import com.example.connectapisupabase.data.model.Book
import com.example.connectapisupabase.data.model.BooksItems
import com.example.connectapisupabase.data.model.ErrorResponse
import com.example.connectapisupabase.data.model.ResponsesAuth
import com.example.connectapisupabase.data.model.UserResponse
import com.example.connectapisupabase.domain.model.UserAuth
import com.example.connectapisupabase.domain.model.UserRequest
import com.example.connectapisupabase.domain.state.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    // Экземпляр ApiService для выполнения API-запросов
    private val apiService = ApiService.instance

    // Хранит токен аутентификации
    private val _token = mutableStateOf("")

    // Состояние результата операций

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    // Список книг, полученных из API
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> get() = _books.asStateFlow()

    // Метод для аутентификации пользователя
    fun signIn() {
        _resultState.value = ResultState.Loading // Устанавливаем состояние загрузки
        viewModelScope.launch {
            apiService.signIn(UserAuth("test@test.ru", "11111111")).enqueue(object :
                Callback<ResponsesAuth> {
                override fun onResponse(
                    call: Call<ResponsesAuth>,
                    response: Response<ResponsesAuth>,
                ) {
                    try {
                        if (response.isSuccessful) {
                            // Если запрос успешен, сохраняем токен и обновляем состояние
                            _token.value = response.body()!!.token
                            _resultState.value = ResultState.Success("Success")
                            Log.d("SignIN", _token.value)
                        } else {
                            // Обработка ошибки, если запрос не успешен
                            response.errorBody()?.string().let {
                                try {
                                    val message = Gson().fromJson(it, ErrorResponse::class.java).message
                                    Log.e("SignIN", "Error message: $message")
                                    _resultState.value = ResultState.Error(message)
                                } catch (e: Exception) {
                                    Log.e("SignIN", "Failed to parse error response: ${e.message}")
                                }
                            }
                        }
                    } catch (exception: Exception) {
                        _resultState.value = ResultState.Error(exception.message.toString())
                        Log.e("SignIN catch", exception.message.toString())
                    }
                }

                override fun onFailure(call: Call<ResponsesAuth>, t: Throwable) {
                    // Обработка ошибки при выполнении запроса
                    _resultState.value = ResultState.Error(t.message.toString())
                    Log.e("SignIN onFailure", t.message.toString())
                }
            })
        }
    }

    // Метод для регистрации нового пользователя
    fun signUp() {
        _resultState.value = ResultState.Loading // Устанавливаем состояние загрузки
        viewModelScope.launch {
            apiService.signUp(
                UserRequest(
                    email = "newtest@test.ru",
                    password = "11111111",
                    passwordConfirm = "11111111"
                )
            ).enqueue(
                object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>,
                    ) {
                        try {
                            if (response.isSuccessful) {
                                // Если запрос успешен, сохраняем ID пользователя и обновляем состояние
                                val id = response.body()!!.id
                                _resultState.value = ResultState.Success("Success")
                                Log.d("SignUP", id)
                            } else {
                                response.errorBody()?.string().let {
                                    try {
                                        val message = Gson().fromJson(it, ErrorResponse::class.java).message
                                        Log.e("getBooks", "Error message: $message")
                                        _resultState.value = ResultState.Error(message)
                                    } catch (e: Exception) {
                                        Log.e("getBooks", "Failed to parse error response: ${e.message}")
                                    }
                                }
                            }
                        } catch (exception: Exception) {
                            _resultState.value = ResultState.Error(exception.message.toString())
                            Log.e("SignUP", exception.message.toString())
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        // Обработка ошибки при выполнении запроса
                        _resultState.value = ResultState.Error(t.message.toString())
                        Log.e("SignUP", t.message.toString())
                    }
                })
        }
    }

    // Метод для получения списка книг
    fun getBooks() {
        _resultState.value = ResultState.Loading // Устанавливаем состояние загрузки
        viewModelScope.launch {
            apiService.getBooks(_token.value).enqueue(object : Callback<BooksItems> {
                override fun onResponse(call: Call<BooksItems>, response: Response<BooksItems>) {
                    try {
                        if (response.isSuccessful) {
                            // Если запрос успешен, сохраняем список книг и обновляем состояние
                            _books.value = response.body()!!.items
                            _books.value.forEach {
                                Log.d("Books", it.title)
                            }
                            _resultState.value = ResultState.Success("Success")
                        } else {
                            // Обработка ошибки, если запрос не успешен
                            response.errorBody()?.string().let {
                                try {
                                    val message = Gson().fromJson(it, ErrorResponse::class.java).message
                                    Log.e("getBooks", "Error message: $message")
                                    _resultState.value = ResultState.Error(message)
                                } catch (e: Exception) {
                                    Log.e("getBooks", "Failed to parse error response: ${e.message}")
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        _resultState.value = ResultState.Error(ex.message.toString())
                        Log.e("getBooks", ex.message.toString())
                    }
                }

                override fun onFailure(call: Call<BooksItems>, t: Throwable) {
                    // Обработка ошибки при выполнении запроса
                    _resultState.value = ResultState.Error(t.message.toString())
                    Log.e("getBooks", t.message.toString())
                }
            })
        }
    }

    // Метод для получения URL изображения книги
    fun getImage(book: Book): String {
        val imageUrl = "http://10.0.2.2:8090/api/files/${book.collectionId}/${book.id}/${book.image}"
        Log.i("Image", imageUrl)
        return imageUrl
    }
}
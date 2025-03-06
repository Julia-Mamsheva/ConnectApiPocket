package com.example.connectapisupabase.domain.state

sealed class ResultState {
    data object Loading:ResultState()
    data object Initialized:ResultState()
    data class Success(val message:String):ResultState()
    data class Error(val message:String):ResultState()
}
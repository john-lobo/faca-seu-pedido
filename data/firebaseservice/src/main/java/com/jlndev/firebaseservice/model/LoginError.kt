package com.jlndev.firebaseservice.model

sealed class LoginError(message: String) : Throwable(message) {
    object UserNotFound : LoginError("Usuário não encontrado.")
    object InvalidCredentials : LoginError("E-mail ou senha inválida.")
    object NetworkError : LoginError("Erro de rede. Verifique sua conexão com a internet.")
    object UnknownError : LoginError("Erro desconhecido. Tente novamente mais tarde.")
}
package com.jlndev.firebaseservice.model

sealed class LoginWithGoogleError(message: String) : Throwable(message) {
    object UserNotFound : LoginWithGoogleError("Conta do Google não encontrada ou desativada.")
    object InvalidCredentials : LoginWithGoogleError("E-mail ou senha inválida.")
    object NetworkError : LoginWithGoogleError("Erro de rede. Verifique sua conexão com a internet.")
    object UnknownError : LoginWithGoogleError("Erro desconhecido. Tente novamente mais tarde.")
    object GoogleAccountEmailAlreadyInUseError : LoginWithGoogleError("Conta já existente com o mesmo e-mail.")
}

package com.jlndev.firebaseservice.model

sealed class RegistrationError(message: String): Exception(message) {
    object EmailAlreadyExists : RegistrationError("O email já está em uso. Tente com outro e-mail.")
    object WeakPassword : RegistrationError("A senha é muito fraca. Tente com uma senha mais forte.")
    object NetworkError : RegistrationError("Erro de rede. Verifique sua conexão com a internet.")
    object UnknownError : RegistrationError("Erro desconhecido. Tente novamente mais tarde.")
    object InvalidEmail : RegistrationError("E-mail inválido.")
    object EmailVerificationFailed : RegistrationError("Falha ao verificar e-mail.")
}
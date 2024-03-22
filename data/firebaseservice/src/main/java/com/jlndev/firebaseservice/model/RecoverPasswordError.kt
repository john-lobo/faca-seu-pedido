package com.jlndev.firebaseservice.model

sealed class RecoverPasswordError(message: String) : Throwable(message) {

    object NetworkError : RecoverPasswordError("Erro de rede. Verifique sua conexão com a internet.")

    object UnknownError : RecoverPasswordError("Erro desconhecido. Tente novamente mais tarde.")

    object GenericError : RecoverPasswordError("Ocorreu um erro ao enviar o código. Tente novamente")

    object EmailNotFound : RecoverPasswordError("E-mail não encontrado. Utilize um e-mail cadastrado!")


}
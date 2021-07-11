package com.dewival.testdewival.ui.viewmodel

import com.dewival.testdewival.network.TokenModel

sealed class RegistrationResult
data class ValidResult(val tokenModel: TokenModel):RegistrationResult()
data class ErrorResult(val e:Throwable):RegistrationResult()
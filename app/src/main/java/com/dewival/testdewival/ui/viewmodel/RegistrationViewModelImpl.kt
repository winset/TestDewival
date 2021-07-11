package com.dewival.testdewival.ui.viewmodel

import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dewival.testdewival.repository.RegistrationRepository
import com.dewival.testdewival.repository.RegistrationRepositoryImpl
import com.dewival.testdewival.ui.models.UserUi
import com.dewival.testdewival.utils.toBase64

import kotlinx.coroutines.launch

internal class RegistrationViewModelImpl(
    private val registrationRepository: RegistrationRepository = RegistrationRepositoryImpl()
) : RegistrationViewModel() {

    override val result = MutableLiveData<RegistrationResult>()

    init {
        checkIsUserAlreadyLogIn()
    }

    //TODO(The model in this func is named userUi but it is not a userUi, you need to add domain layer models)
    //This is a big problem for the whole code in terms of clean
    //but too little time to fix it
    private fun logIn(userUI: UserUi) {
        viewModelScope.launch {
            try {
                val token = registrationRepository.getToken(userUI.login, userUI.password)
                registrationRepository.addUserToDb(UserUi(userUI.login, userUI.password))
                result.value = ValidResult(token)
            } catch (e: Throwable) {
                result.value = ErrorResult(e)
            }
        }
    }

    override fun logInUI(userUI: UserUi) {
        val base64 = toBase64(userUI.password)
        val user = UserUi(userUI.login,base64)
        logIn(user)
    }

    private fun checkIsUserAlreadyLogIn() {
        viewModelScope.launch {
            val user = registrationRepository.getUserIfExist()
            if (user != null) {
                logIn(user)
            }
        }
    }
}
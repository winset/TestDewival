package com.dewival.testdewival.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dewival.testdewival.ui.models.UserUi


internal abstract class RegistrationViewModel : ViewModel() {

    abstract val result: LiveData<RegistrationResult>
    abstract fun logInUI(userUI: UserUi)
}
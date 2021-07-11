package com.dewival.testdewival.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

internal abstract class SenderViewModel : ViewModel() {
    abstract val result: LiveData<SenderResult>
    abstract fun uploadImage(token: String, path: String)
}
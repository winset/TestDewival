package com.dewival.testdewival.ui.viewmodel

import java.lang.Exception

sealed class SenderResult
object Success : SenderResult()
data class ErrorSenderResult(val e:Exception):SenderResult()

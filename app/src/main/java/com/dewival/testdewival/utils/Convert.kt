package com.dewival.testdewival.utils

import android.util.Base64

fun toBase64(string: String): String {
    val data: ByteArray = string.toByteArray(Charsets.UTF_8)
    return Base64.encodeToString(data, Base64.DEFAULT)
}
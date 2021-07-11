package com.dewival.testdewival.repository

import com.dewival.testdewival.network.TokenModel
import com.dewival.testdewival.ui.models.UserUi

interface BaseRepository {
    suspend fun getToken(login: String, password: String): TokenModel
    suspend fun getUserIfExist(): UserUi?
}
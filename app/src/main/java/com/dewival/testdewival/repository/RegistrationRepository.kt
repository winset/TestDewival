package com.dewival.testdewival.repository

import com.dewival.testdewival.network.TokenModel
import com.dewival.testdewival.ui.models.UserUi


interface RegistrationRepository:BaseRepository {

    suspend fun addUserToDb(userUI: UserUi)


}
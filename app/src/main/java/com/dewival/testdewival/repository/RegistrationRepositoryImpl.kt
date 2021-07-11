package com.dewival.testdewival.repository

import com.dewival.testdewival.db.BaseCacheDataSource
import com.dewival.testdewival.db.BaseRealmProvider
import com.dewival.testdewival.db.UserEntity
import com.dewival.testdewival.network.DewivalApi
import com.dewival.testdewival.network.NetworkModule
import com.dewival.testdewival.network.RegistrationBody
import com.dewival.testdewival.network.TokenModel
import com.dewival.testdewival.ui.models.UserUi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegistrationRepositoryImpl(
    private val api: DewivalApi = NetworkModule.api,
    private val baseCacheDataSource: BaseCacheDataSource = BaseCacheDataSource(BaseRealmProvider())
) : RegistrationRepository {

    override suspend fun getToken(login: String, password: String): TokenModel {
        return withContext(Dispatchers.IO) {
            TokenModel(api.getToken(RegistrationBody(login, password)))
        }
    }

     override suspend fun addUserToDb(userUI: UserUi) {
        baseCacheDataSource.addUser(UserEntity.uiToEntity(userUI))
    }

    override suspend fun getUserIfExist():UserUi?{
        return baseCacheDataSource.getUser()?.mapToUi()
    }
}
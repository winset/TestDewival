package com.dewival.testdewival.repository

import android.util.Log
import com.dewival.testdewival.db.BaseCacheDataSource
import com.dewival.testdewival.db.BaseRealmProvider
import com.dewival.testdewival.db.PhotoEntity
import com.dewival.testdewival.network.DewivalApi
import com.dewival.testdewival.network.NetworkModule
import com.dewival.testdewival.network.RegistrationBody
import com.dewival.testdewival.network.TokenModel
import com.dewival.testdewival.repository.models.Photo
import com.dewival.testdewival.ui.models.UserUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class SenderRepositoryImpl(
    private val api: DewivalApi = NetworkModule.api,
    private val baseCacheDataSource: BaseCacheDataSource = BaseCacheDataSource(BaseRealmProvider())
) : SenderRepository {

    override suspend fun getToken(login: String, password: String): TokenModel {
        return withContext(Dispatchers.IO) {
            TokenModel(api.getToken(RegistrationBody(login, password)))
        }
    }

    override suspend fun uploadImage(tokenModel: TokenModel, imageFile: MultipartBody.Part) {
        withContext(Dispatchers.IO) {
            val path = BaseRealmProvider().provide().path
            val file = File(path)
            val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
            val realmFileToUpload: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file.name, requestBody)
            Log.d("TAG", "REALM: ${path}")
            api.sendFile(tokenModel.token, imageFile, realmFileToUpload)
        }
    }

    override suspend fun savePhotoInfo(photo: Photo) {
        baseCacheDataSource.addPhotoInfo(PhotoEntity.uiToEntity(photo))
    }

    override suspend fun getUserIfExist(): UserUi {
        return baseCacheDataSource.getUser()?.mapToUi()!!
    }
}
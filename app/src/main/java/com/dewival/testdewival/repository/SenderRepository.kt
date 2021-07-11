package com.dewival.testdewival.repository

import com.dewival.testdewival.network.TokenModel
import com.dewival.testdewival.repository.models.Photo
import okhttp3.MultipartBody

interface SenderRepository:BaseRepository {
    suspend fun uploadImage(tokenModel: TokenModel, imageFile:MultipartBody.Part)
    suspend fun savePhotoInfo(photo: Photo)
}
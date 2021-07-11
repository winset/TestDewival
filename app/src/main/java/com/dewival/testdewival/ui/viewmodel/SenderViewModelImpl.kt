package com.dewival.testdewival.ui.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dewival.testdewival.network.ApiInterceptor
import com.dewival.testdewival.network.TokenModel
import com.dewival.testdewival.repository.SenderRepository
import com.dewival.testdewival.repository.SenderRepositoryImpl
import com.dewival.testdewival.repository.models.Photo
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception

internal class SenderViewModelImpl(
    private val senderRepository: SenderRepository = SenderRepositoryImpl()
) : SenderViewModel() {

    override val result = MutableLiveData<SenderResult>()

    override fun uploadImage(token: String, path: String) {
        val file = File(path)

        val fileToUpload = getFileToUpload(file)
        val photoInfo = Photo(file.name, file.lastModified(), System.currentTimeMillis())

        //TODO(part of errors can be handle in repository)
        viewModelScope.launch {
            try {
                senderRepository.savePhotoInfo(photoInfo)
                senderRepository.uploadImage(TokenModel(token), fileToUpload)
                result.value = Success
            } catch (e: Exception) {
                if (e.message == "${ApiInterceptor.HTTP_401} ${ApiInterceptor.AUTH_FAILED}") {       // This part)
                    try {
                        val currentUser = senderRepository.getUserIfExist()!!
                        val token =
                            senderRepository.getToken(currentUser.login, currentUser.password)
                        senderRepository.uploadImage(token, fileToUpload)
                        result.value = Success
                    } catch (e: Exception) {
                        result.value = ErrorSenderResult(e)
                    }
                } else {
                    result.value = ErrorSenderResult(e)
                    Log.d(
                        "TAG",
                        "uploadImage Error : ${e.javaClass}  " + e.message + "  ${e.cause}  ${e.stackTrace}"
                    )
                }
            }
        }
    }


    private fun getFileToUpload(file: File): MultipartBody.Part {
        val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
        val fileToUpload: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestBody)
        return fileToUpload
    }
}
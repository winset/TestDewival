package com.dewival.testdewival.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BaseCacheDataSource(
    private val realmProvider: RealmProvider
) {

    suspend fun addUser(userEntity: UserEntity) {
        withContext(Dispatchers.IO) {
            realmProvider.provide().executeTransaction {
                it.insertOrUpdate(userEntity)
            }
        }
    }

    suspend fun getUser(): UserEntity? {
        return realmProvider.provide().where(UserEntity::class.java).findFirst()
    }

    suspend fun addPhotoInfo(photoEntity: PhotoEntity){
        withContext(Dispatchers.IO) {
            realmProvider.provide().executeTransaction {
                val newId: Number? = it.where(photoEntity::class.java).max("id")
                if (newId != null) {
                    photoEntity.id = newId.toInt()+1
                }else{
                    photoEntity.id = 0
                }

                it.insertOrUpdate(photoEntity)
            }
        }
    }
}
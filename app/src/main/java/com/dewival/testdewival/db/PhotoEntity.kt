package com.dewival.testdewival.db

import com.dewival.testdewival.repository.models.Photo
import com.dewival.testdewival.ui.models.UserUi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PhotoEntity : RealmObject() {
    @PrimaryKey
    var id: Int = -1
    var name: String = ""
    var created: Long = 0
    var send: Long = 0


    //TODO(bad code but no time to write mappers and models for each layer)
    fun mapToPhoto(): Photo {
        return Photo(name, created, send)
    }

    companion object {
        fun uiToEntity(photo: Photo): PhotoEntity {
            val entity = PhotoEntity()
            entity.name = photo.name
            entity.created = photo.created
            entity.send = photo.send
            return entity
        }
    }
}
package com.dewival.testdewival.db

import com.dewival.testdewival.ui.models.UserUi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserEntity:RealmObject() {
    @PrimaryKey
    var login:String = ""
    var password:String = ""



    //TODO(bad code but no time to write mappers and models for each layer)
    fun mapToUi(): UserUi {
        return UserUi(login,password)
    }

    companion object{
        fun uiToEntity(userUI: UserUi): UserEntity {
            val entity = UserEntity()
            entity.login = userUI.login
            entity.password = userUI.password
            return entity
        }
    }
}
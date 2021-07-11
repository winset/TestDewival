package com.dewival.testdewival.db

import io.realm.Realm

interface RealmProvider {
    fun provide():Realm
}
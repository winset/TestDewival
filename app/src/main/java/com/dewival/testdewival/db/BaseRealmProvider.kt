package com.dewival.testdewival.db

import io.realm.Realm

class BaseRealmProvider:RealmProvider {
    override fun provide(): Realm = Realm.getDefaultInstance()
}
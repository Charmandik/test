package ru.bikbulatov.pikabutest.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PostModel(id: Int = 0, title: String = "") : RealmObject() {
    @PrimaryKey
    var id: Int = id
    var title: String = title
}
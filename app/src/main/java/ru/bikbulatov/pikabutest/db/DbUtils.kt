package ru.bikbulatov.pikabutest.db

import io.realm.Realm
import io.realm.RealmResults
import ru.bikbulatov.pikabutest.BaseApp
import ru.bikbulatov.pikabutest.data.models.Post

object DbUtils {
    private var realm: Realm = BaseApp().getRealm()

    fun savePost(id: Int, title: String) {
        realm.executeTransaction {
            realm.insertOrUpdate(PostModel(id, title))
        }
    }

    fun deletePost(id: Int) {
        realm.executeTransaction {
            realm.where(PostModel::class.java).equalTo("id", id).findAll()?.deleteAllFromRealm()
        }
    }

    fun isPostSaved(id: Int): Boolean {
        return realm.where(PostModel::class.java).equalTo("id", id).findFirst() != null
    }

    fun getSavedPostsAsCopy(): MutableList<PostModel>? {
        return BaseApp().getRealm()
            .copyFromRealm(BaseApp().getRealm().where(PostModel::class.java).findAll())
    }

    fun getSavedPosts(): RealmResults<PostModel>? {
        return BaseApp().getRealm().where(PostModel::class.java).findAll()
    }

    fun convertDbToDataClass(list: MutableList<PostModel>): List<Post> {
        val newList: MutableList<Post> = mutableListOf()
        list.forEach {
            newList.add(Post(it.id, it.title, null, null))
        }
        return newList.toList()
    }
}
package ru.bikbulatov.pikabutest.domain

import androidx.lifecycle.MutableLiveData
import ru.bikbulatov.pikabutest.data.models.Post
import ru.bikbulatov.pikabutest.db.PostModel
import ru.bikbulatov.pikabutest.helpers.Event

interface PostRepo {
    fun getPosts(postsList: MutableLiveData<Event<List<Post>>>)
    fun getPost(id: Int, post: MutableLiveData<Event<Post>>)
    fun getSavedPosts(savedPostsList: MutableLiveData<MutableList<PostModel>>)
}
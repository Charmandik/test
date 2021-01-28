package ru.bikbulatov.pikabutest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.bikbulatov.pikabutest.data.api.IPostsApi
import ru.bikbulatov.pikabutest.data.models.Post
import ru.bikbulatov.pikabutest.db.PostModel
import ru.bikbulatov.pikabutest.domain.PostRepo
import ru.bikbulatov.pikabutest.helpers.Event

class PostsViewModel @ViewModelInject constructor(var postsApi: IPostsApi, var postRepo: PostRepo) :
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    var postsList: MutableLiveData<Event<List<Post>>> = MutableLiveData()
    fun getPosts() {
        postRepo.getPosts(postsList)
        postsList.postValue(Event.loading())
    }

    var post: MutableLiveData<Event<Post>> = MutableLiveData()
    fun getPost(id: Int) {
        postRepo.getPost(id, post)
    }

    var savedPostsList: MutableLiveData<MutableList<PostModel>> = MutableLiveData()
    fun getSavedPosts() {
        postRepo.getSavedPosts(savedPostsList)
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }
}
package ru.bikbulatov.pikabutest.data

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.bikbulatov.pikabutest.data.api.IPostsApi
import ru.bikbulatov.pikabutest.data.models.Post
import ru.bikbulatov.pikabutest.db.DbUtils
import ru.bikbulatov.pikabutest.db.PostModel
import ru.bikbulatov.pikabutest.domain.PostRepo
import ru.bikbulatov.pikabutest.helpers.Event
import javax.inject.Inject

class PostsRepoImpl @Inject constructor(var postsApi: IPostsApi) : PostRepo {
    private val compositeDisposable = CompositeDisposable()
    override fun getPosts(postsList: MutableLiveData<Event<List<Post>>>) {
        compositeDisposable.add(
            postsApi.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<List<Post>>() {
                    override fun onNext(response: List<Post>) {
                        response.let {
                            postsList.postValue(Event.success(response))
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.let {
                            if (it.message?.contains("Unable to resolve host")!!)
                                postsList.postValue(Event.error(Error("Отсутствует подключение к интернету. Попробуйте повторить позже.")))
                            else
                                postsList.postValue(Event.error(Error("При отправке запроса произошла ошибка. Попробуйте повторить позже.")))
                        }
                    }


                    override fun onComplete() {
                    }
                })
        )
    }

    override fun getPost(id: Int, post: MutableLiveData<Event<Post>>) {
        post.value = null
        post.postValue(Event.loading())
        compositeDisposable.add(
            postsApi.getPost(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<Post>() {
                    override fun onNext(response: Post) {
                        response.let {
                            post.postValue(Event.success(response))
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.let {
                            if (it.message?.contains("Unable to resolve host")!!)
                                post.postValue(Event.error(Error("Отсутствует подключение к интернету. Попробуйте повторить позже.")))
                            else
                                post.postValue(Event.error(Error("При отправке запроса произошла ошибка. Попробуйте повторить позже.")))
                        }
                    }


                    override fun onComplete() {
                    }
                })
        )
    }

    override fun getSavedPosts(savedPostsList: MutableLiveData<MutableList<PostModel>>) {
        savedPostsList.postValue(DbUtils.getSavedPostsAsCopy())
    }
}
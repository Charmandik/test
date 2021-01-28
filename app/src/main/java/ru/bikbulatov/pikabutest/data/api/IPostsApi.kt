package ru.bikbulatov.pikabutest.data.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.bikbulatov.pikabutest.data.models.Post

interface IPostsApi {
    /**
     * Load posts
     * @return List with posts
     */
    @GET("feed.php")
    fun getPosts(): Observable<List<Post>>

    /**
     * Load post
     * @param id
     * @return List with posts
     */
    @GET("story.php")
    fun getPost(@Query("id") id: Int): Observable<Post>

}
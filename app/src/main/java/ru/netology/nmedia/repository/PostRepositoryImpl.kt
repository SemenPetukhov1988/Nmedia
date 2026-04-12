package ru.netology.nmedia.repository

import java.util.concurrent.TimeUnit
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.lang.reflect.Type

class PostRepositoryImpl () : PostRepository  {

    private  val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"

        val jsonType = "application/json".toMediaType()

        val postType: Type = object : TypeToken<List<Post>> () {}.type
    }

    override fun repostById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun likeById(id: Long) : Post {
// создаем адрес запроса
         val url = "${BASE_URL}api/posts/$id/likes"
        // создаем запрос
       val call = client.newCall(
         request = Request.Builder()
            . url(url)
            .post("".toRequestBody(jsonType))
            .build()
        )
        val response = call.execute()
        val stringResponse = response.body.string()
        return gson.fromJson(stringResponse, Post::class.java)

    }

    override fun unLikeById(id: Long): Post {
        val url = "${BASE_URL}api/posts/$id/likes"
        // создаем запрос
        val call = client.newCall(
            request = Request.Builder()
                . url(url)
                .delete()
                .build()
        )
        val response = call.execute()
        val stringResponse = response.body.string()
        return gson.fromJson(stringResponse, Post::class.java)
    }

    override fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun save(post: Post) : Post {
        val call =  client.newCall(
            Request.Builder()
                .url("${BASE_URL}api/slow/posts")
                .post(gson.toJson(post).toRequestBody(jsonType))
                .build()
        )
        val response = call.execute()

        val stringResponse= response.body.string()

        return gson.fromJson(stringResponse,Post ::class.java)


    }

    override fun updatePost(id: Long?, content: String) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Post> {
       val call =  client.newCall(
            Request.Builder()
                .url("${BASE_URL}api/slow/posts")
                .build()
        )
        val response = call.execute()

        val stringResponse= response.body.string()

       return gson.fromJson(stringResponse,postType)
    }
}
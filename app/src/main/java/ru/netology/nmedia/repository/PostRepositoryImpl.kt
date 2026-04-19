package ru.netology.nmedia.repository

import android.util.Log
import java.util.concurrent.TimeUnit
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.lang.reflect.Type

class PostRepositoryImpl() : PostRepository {


    override fun repostById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun likeById(id: Long): Post? {
        if ((0..1).random() == 0) {
            throw IOException("Сервер недоступен (имитация ошибки 5xx)")
        }
        Log.d("PostRepository", "Отправляем запрос на лайк для поста ID: $id")
        val response = PostApi.servise.likeById(id).execute()
        Log.d(
            "PostRepository",
            "Получен ответ для likeById($id): код ${response.code()}, успешен: ${response.isSuccessful}"
        )
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("PostRepository", "Ошибка likeById: ${response.code()} ${response.message()}")
            null
        }
    }

    override fun unLikeById(id: Long): Post? {
        if ((0..1).random() == 0) {
            throw IOException("Сервер недоступен (имитация ошибки 5xx)")
        }
        Log.d("PostRepository", "Отправляем запрос на дизлайк для поста ID: $id")
        val response = PostApi.servise.dislikeById(id).execute()
        Log.d(
            "PostRepository",
            "Получен ответ для dislikeById($id): код ${response.code()}, успешен: ${response.isSuccessful}"
        )
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("PostRepository", "Ошибка dislikeById: ${response.code()} ${response.message()}")
            null
        }
    }

    override fun removeById(id: Long) {
        //имитация ошибки в 50 процентах случаев
        if ((0..1).random() == 0) {
            throw IOException("Сервер недоступен (имитация ошибки 5xx)")
        }
        PostApi.servise.removeById(id)
            .execute()

    }

    override fun save(post: Post) {
        if ((0..1).random() == 0) {
            throw IOException("Сервер недоступен (имитация ошибки 5xx)")
        }
        PostApi.servise.save(post)
            .execute()

    }

    override fun updatePost(id: Long?, content: String) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Post> {
        if ((0..1).random() == 0) {
            throw okio.IOException("Сервер недоступен(имитация ошибки 5хх)")
        }
        return PostApi.servise.getAll()
            .execute()
            .body()
            .orEmpty()
    }
}
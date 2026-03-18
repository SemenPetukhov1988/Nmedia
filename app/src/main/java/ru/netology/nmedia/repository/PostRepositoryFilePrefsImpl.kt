package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

//3. Реализуем интерфейс репозитория

// создали класс наследуемый через двоеточие от интерфейса

class PostRepositoryFilePrefsImpl(private val context: Context) : PostRepository {

    private var nextId = 1L

    var posts = emptyList<Post>()
        set(value) {
            field = value
            sunc()
        }

    override fun getData(): LiveData<List<Post>> = data
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, token)
                nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
                data.value = posts
            }
        }
            else {
                posts = listOf(Post(20,"semen", "10", "hello"))
                data.value = posts
            }

    }

    private fun sunc() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {

            it.write(gson.toJson(posts))
        }
    }


    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likeQuantity = if (it.likedByMe) (it.likeQuantity- 1) else (it.likeQuantity + 1)
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {

            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    data = "now"
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }


    override fun updatePost(id: Long?, content: String) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(content = content) // Копируем только контент, оставляя остальные поля неизменными
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> {
        TODO("Not yet implemented")
    }

    // функция репоста
    override fun repostById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else if (!it.repostByMe) {
                it.copy(
                    repostByMe = true, // Теперь пользователь репостнул
                    repostQality = (it.repostQality+ 1) // Увеличение количества репостов
                )
            } else {
                it
            }
        }
        data.value = posts
    }

    companion object {
        private const val FILE_NAME = "pos.json"
        private val gson = Gson()
        private val token =
            TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}









package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okio.IOException
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

import kotlin.concurrent.thread

//4. создаем класс чисто для работы изменения данных во вьюшках, такая модель чтобы данные были стабильными и не терялись при работе андройда

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = 0,
    likes = 12000,
    likedByMe = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())

    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()

    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun save(content: String) {
        thread {
            edited.value?.let {
                val text = content.trim()
                if (it.content != text) {
                    repository.save(it.copy(content = text))
                }
            }
            _postCreated.postValue(Unit)
            edited.postValue(empty)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun load() {
        // работа на фоновом потоке
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            } catch (e: IOException) {
                _data.postValue(FeedModel(error = true))

            }
        }
    }

    fun likeById(id: Long) {
        thread {
            val currentPost = _data.value?.posts?.find { it.id == id }
            if (currentPost == null) {
                // Можно добавить лог или просто выйти
                return@thread
            }
            val updatedPost = if (currentPost.likedByMe) {
                repository.unLikeById(id)
            } else {
                repository.likeById(id)
            }

            val currentPosts = _data.value!!.posts.toMutableList()
            val index = currentPosts.indexOfFirst { it.id == id }
            if (index != -1) {
                currentPosts[index] = updatedPost
                _data.postValue(_data.value!!.copy(posts = currentPosts))

            }
        }

    }
    fun removeById(id: Long) = repository.removeById(id)
    fun repostById(id: Long) = repository.repostById(id)
}
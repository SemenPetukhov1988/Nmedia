package ru.netology.nmedia.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okio.IOException
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.SaveModel
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

    val dataSave = MutableLiveData(SaveModel())

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
            dataSave.postValue(SaveModel(loading = true))
            try {

                edited.value?.let {
                    val text = content.trim()
                    if (it.content != text) {
                        repository.save(it.copy(content = text))
                    }
                }
                _postCreated.postValue(Unit)
                edited.postValue(empty)
                dataSave.postValue((SaveModel(loading = false)))
            } catch (e: Exception) {
                dataSave.postValue(SaveModel(error = true))
            }
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun load() {
        // работа на фоновом потоке
        // если
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

    var isProcessing = false

    fun likeById(id: Long) {
        if (isProcessing) return
        isProcessing = true

        thread {
            // 1. Устанавливаем состояние загрузки
            _data.postValue(_data.value?.copy(loading = true))


            try {
                // 2. Получаем текущее состояние лайка
                val currentLiked = _data.value?.posts?.find { it.id == id }?.likedByMe
                // 3. Отправляем запрос на сервер
                val updatedPost = if (currentLiked == true) {
                    repository.unLikeById(id)
                } else {
                    repository.likeById(id)
                }

                if (updatedPost == null) {
                    // 4. Ошибка: сервер вернул null

                    _data.postValue(
                        _data.value?.copy(
                            loading = false,
                            error = true
                        )
                    )
                } else {
                    // 5. Успех: обновляем UI данными от сервера
                    _data.value?.let { currentData ->
                        val finalPosts = currentData.posts.map { post ->
                            if (post.id == id) updatedPost else post
                        }
                        _data.postValue(
                            currentData.copy(
                                posts = finalPosts,
                                loading = false,  // Сбрасываем загрузку
                                error = false   // Сбрасываем ошибку
                            )
                        )

                    }
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Исключение при обработке лайка: ${e.message}", e)

                // 6. Ошибка сети/исключение: показываем ошибку
                _data.postValue(
                    _data.value?.copy(
                        loading = false,
                        error = true
                    )
                )
            } finally {
                // 7. Всегда сбрасываем isProcessing в конце операции
                isProcessing = false
            }
        }
    }

    fun removeById(id: Long) {
        thread {
            // Устанавливаем состояние загрузки
            _data.postValue(_data.value?.copy(loading = true))

            try {
                repository.removeById(id)

                // Успех: обновляем список постов и сбрасываем флаги
                _data.value?.let { currentData ->
                    val updatedPosts = currentData.posts.filterNot { it.id == id }
                    _data.postValue(
                        currentData.copy(
                            posts = updatedPosts,
                            loading = false,  // Сбрасываем загрузку
                            error = false,   // Сбрасываем ошибку
                            empty = updatedPosts.isEmpty()
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Ошибка удаления поста $id", e)

                // Ошибка: сохраняем текущие данные, но устанавливаем флаг ошибки и сбрасываем загрузку
                _data.postValue(
                    _data.value?.copy(
                        loading = false,  // ОБЯЗАТЕЛЬНО сбрасываем загрузку!
                        error = true     // Устанавливаем флаг ошибки
                    )
                )
            }
        }
    }

    fun repostById(id: Long) = repository.repostById(id)
}
package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFilePrefsImpl
import ru.netology.nmedia.repository.PostRepositoryMemory
import ru.netology.nmedia.repository.PostRepositorySharedPrefsImpl

//4. создаем класс чисто для работы изменения данных во вьюшках, такая модель чтобы данные были стабильными и не терялись при работе андройда

class PostViewModel (application: Application) : AndroidViewModel(application ) {


    private val repository: PostRepository = PostRepositoryFilePrefsImpl(application)

    // создаем переменную куда кладем данные нашего поста
    val data = repository.getData()

    fun save(post: Post) {
        repository.save(post)
    }


    fun repostById(id: Long) = repository.repostById(id)
    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun updatePost(id: Long?, content: String) {
        repository.updatePost(id, content)
    }


}

package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryMemory

//4. создаем класс чисто для работы изменения данных во вьюшках, такая модель чтобы данные были стабильными и не терялись при работе андройда

class PostViewModel : ViewModel() {




    private val repository: PostRepository = PostRepositoryMemory()

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

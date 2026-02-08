package ru.netology.nmedia.viewModel

import android.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryMemory

//4. создаем класс чисто для работы изменения данных во вьюшках, такая модель чтобы данные были стабильными и не терялись при работе андройда

class PostViewModel : ViewModel() {

    val empty = Post(
        id = 0,
        author = "Netology",
        data = "Now",
        content = "1111",
        likedByMe = false,
        likeQuantity = "0",
        repostByMe = false,
        repostQality = "50",
    )


    private val repository: PostRepository = PostRepositoryMemory()

    // создаем переменную куда кладем данные нашего поста
    val data = repository.getData()
    val edited = MutableLiveData(empty)



    fun save(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                repository.save(it.copy(content = text))
            }

        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post

    }

    fun repostById(id: Long) = repository.repostById(id)
    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)



    


}

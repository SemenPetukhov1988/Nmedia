package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryMemory

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryMemory()
    val data = repository.getData()

    fun like() {
        repository.like()
    }
    fun repost() {
        repository.repost()
    }
}

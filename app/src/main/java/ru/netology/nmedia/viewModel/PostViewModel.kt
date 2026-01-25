package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryMemory

//4. создаем класс чисто для работы изменения данных во вьюшках, такая модель чтобы данные были стабильными и не терялись при работе андройда

class PostViewModel : ViewModel() { // если класс такого типа ,
    // то все будет норм данные не изменятся при повороте экрана

    // делаем переменную куда кладем класс нашего репозитория ,
    // чтобы можно было обращаться к его методам
    private val repository: PostRepository = PostRepositoryMemory()
    // создаем переменную куда кладем данные нашего поста
    val data = repository.getData()

    // делаем функции для работы с данными вьюшек

    fun repostById(id: Long) =  repository.repostById(id)
    fun likeById(id: Long) = repository.likeById(id)
}

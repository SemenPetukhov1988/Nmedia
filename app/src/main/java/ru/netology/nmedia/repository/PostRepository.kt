package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

//2. делаем интерфейс где описываем какие функции будут использоваться при работе с постом, думаю это не обязательно , но потом пригодится
interface PostRepository {
    fun getData(): LiveData<List<Post>> // получение данных с типом возвращаемого значкения LiveData<Post>(делается только чтобы стабильно работать с данными )


    fun repostById( id: Long) // функция можно сделать репост

    fun likeById(id: Long)

    fun removeById(id: Long)

    fun save (post: Post)

    fun updatePost(id: Long?, content: String)
}
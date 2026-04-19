package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

//2. делаем интерфейс где описываем какие функции будут использоваться при работе с постом, думаю это не обязательно , но потом пригодится
interface PostRepository {


    fun repostById(id: Long) // функция можно сделать репост

    fun likeById(id: Long) : Post?

    fun unLikeById (id : Long) : Post?

    fun removeById(id: Long)

    fun save(post: Post)

    fun updatePost(id: Long?, content: String)
    fun getAll():List<Post>
}
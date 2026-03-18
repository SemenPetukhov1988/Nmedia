package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getData(): LiveData<List<Post>> {
        TODO("Not yet implemented")
    }

    override fun repostById(id: Long) {
        dao.repostById(id)
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun updatePost(id: Long?, content: String) {
        TODO("Not yet implemented")
    }

    override fun getAll() : LiveData<List<Post>> = dao.getAll().map { list -> list.map {it.toDto()}

    }

}


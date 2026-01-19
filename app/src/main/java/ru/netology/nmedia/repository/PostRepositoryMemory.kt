package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryMemory : PostRepository {
    var post = Post(
        1L, "Нетология. Университет интернет профессий будущего", "13 января 2026 года",
        "«Нетология» специализируется на переподготовке, высшем образовании (совместно с вузами) и дополнительном обучении специалистов в сферах интернет-маркетинга, управления проектами, дизайна и пользовательского интерфейса, программирования, аналитики и data science, финансов, а также творческих профессий и социальных навыков.",
        false, "5199", false, "5199"
    )
    private val data = MutableLiveData<Post>(post)

    override fun getData(): LiveData<Post> = data

    override fun like() {
       post = post.copy(
           likedByMe = !post.likedByMe, likeQuantity = if (post.likedByMe) {
               (post.likeQuantity.toInt()-1).toString()
           } else {
               (post.likeQuantity.toInt()+1).toString()
           }
       )
        data.value = post

    }

    override fun repost() {
        if (!post.repostByMe) { // Проверяем, делал ли уже репост
            post = post.copy(
                repostByMe = true, // Теперь пользователь репостнул
                repostQality = (post.repostQality.toInt() + 1).toString() // Увеличение количества репостов
            )
            data.value = post
        }
    }


}
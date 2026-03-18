package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val data: String,
    var content: String,
    val likeQuantity: Int,
    val likedByMe: Boolean,
    var repostByMe: Boolean = false,
    val repostQality: Int,
    val videoVisibility: Boolean = false,
    val videoUrl: String
) {
    fun toDto() = Post(id, author, data, content,likedByMe,likeQuantity,repostByMe,repostQality,videoVisibility,videoUrl)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.data, dto.content, dto.likeQuantity, dto.likedByMe,dto.repostByMe,dto.repostQality, dto.videoVisibility,dto.videoUrl)
    }
}

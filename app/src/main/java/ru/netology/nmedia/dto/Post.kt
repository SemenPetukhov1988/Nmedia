package ru.netology.nmedia.dto


data class Post(
    val id: Long,
    val author: String,
    val data: String,
    var content: String,
    val likedByMe: Boolean = false,
    val likeQuantity: Int = 120000,
    var repostByMe: Boolean = false,
    val repostQality: Int = 50,
    val videoVisibility: Boolean = false,
    val videoUrl: String = ""
    )

package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val data: String,
    val content: String,
    val likedByMe: Boolean = false,
    val likeQuantity: String = "1000",
    val repostByMe : Boolean = false,
    val repostQality : String = "50",

)

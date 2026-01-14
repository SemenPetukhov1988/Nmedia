package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val data: String,
    val content: String,
    var likedByMe: Boolean = false,
    var likeQuantity: String = "1000",
    var repostByMe : Boolean = false,
    var repostQality : String = "50",

)

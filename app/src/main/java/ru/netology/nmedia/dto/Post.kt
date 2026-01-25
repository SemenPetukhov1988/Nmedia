package ru.netology.nmedia.dto

// 1. создаем дата класс , где обозначаем из каких элементов состоит пост
data class Post(
    val id: Long,
    val author: String,
    val data: String,
    val content: String,
    val likedByMe: Boolean = false,
    val likeQuantity: String = "1000",
    var repostByMe : Boolean = false,
    val repostQality : String = "50",

    )

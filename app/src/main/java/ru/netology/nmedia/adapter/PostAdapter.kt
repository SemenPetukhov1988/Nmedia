package ru.netology.nmedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.activity.MainActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostActivity
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository

import kotlin.math.ceil

interface OnInteractionListener {
    fun onLike(post: Post)
    fun OnRepost(post: Post)
    fun OnRemove(post: Post)
    fun onEdit(post: Post)
    fun onVideo(video: String)
    fun onShowFulText(post: Post)

}


class PostAdapter(
    private val editPostLauncher: ActivityResultLauncher<Intent>,
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostViewHolder.PostDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, editPostLauncher,onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


fun formatnumber(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 10000 -> {
            val thousands = count / 1000
            val hundreds = (count % 1000) / 100
            "${thousands}.${hundreds}K"
        }

        count < 1000000 -> {
            val thousands = count / 1000
            "$thousands K"
        }

        else -> {
            val millions = count / 1000000
            val hundredThousands = (count % 1000000) / 100000
            "${millions}.${hundredThousands}M"
        }
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val editPostLauncher: ActivityResultLauncher<Intent>,
    private val onInteractionListener: OnInteractionListener


) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) = with(binding) {
        content.text = post.content
        data.text = post.data
        autor.text = post.author
        like.text = formatnumber(post.likeQuantity.toInt())
        repost.text = formatnumber(post.repostQality.toInt())

        repost.isChecked = post.repostByMe
        like.isChecked = post.likedByMe

        if (post.videoVisibility) {
            binding.video.visibility = View.VISIBLE

        }

        video.setOnClickListener {
            onInteractionListener.onVideo(post.videoUrl)

        }

        repost.setOnClickListener {
            if (!post.repostByMe) {
                onInteractionListener.OnRepost(post)
            }


        }

        like.setOnClickListener { onInteractionListener.onLike(post) }

        menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.OnRemove(post)
                            true
                        }

                        R.id.edit -> {
                            val intent = Intent(it.context, NewPostActivity::class.java)
                            intent.putExtra(Intent.EXTRA_TEXT, post.content)
                            intent.putExtra("POST_ID", post.id)
                            editPostLauncher.launch(intent) // Используем лончер
                            true


                        }

                        else -> false

                    }
                }

            }.show()
        }
        repost.isEnabled = !post.repostByMe
    }


    object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

    }
}
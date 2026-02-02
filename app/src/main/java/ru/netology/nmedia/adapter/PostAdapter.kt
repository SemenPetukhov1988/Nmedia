package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.MainActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository

import kotlin.math.ceil

interface OnInteractionListener {
    fun onLike(post: Post)
    fun OnRepost(post: Post)
    fun OnRemove(post: Post)
    fun onEdit(post: Post)

    fun onShowFulText(post: Post)

}


class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostViewHolder.PostDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
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
    private val onInteractionListener: OnInteractionListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) = with(binding) {
        content.text = post.content
        data.text = post.data
        autor.text = post.author
        likeQuantity.text = formatnumber(post.likeQuantity.toInt())
        repostQuantity.text = formatnumber(post.repostQality.toInt())

        repost.setImageResource(
            if (post.repostByMe) R.drawable.share_77929 else R.drawable._share_90177
        )

        like.setImageResource(
            if (post.likedByMe) R.drawable.like_red else R.drawable.heart_icon_icons_com_71176
        )

        if (post.content.length > 400) {
            readMoreButton.visibility = View.VISIBLE
        } else {
            readMoreButton.visibility = View.GONE
        }
        readMoreButton.setOnClickListener {
            onInteractionListener.onShowFulText(post)
        }




            repost.setOnClickListener {
                post.repostByMe = false
                onInteractionListener.OnRepost(post)


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
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false

                        }
                    }

                }.show()
            }
        }


        object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

        }
    }
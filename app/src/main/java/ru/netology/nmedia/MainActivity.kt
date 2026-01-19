package ru.netology.nmedia

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyinset(binding)
        viewModel.data.observe(this) { post ->
            with(binding) {
                binding.content.text = post.content
                binding.data.text = post.data
                binding.autor.text = post.author
                binding.likeQuantity.text = formatnumber(post.likeQuantity.toInt())
                binding.repostQuantity.text = formatnumber(post.repostQality.toInt())
                if (post.likedByMe){
                    like?.setImageResource(R.drawable.like_red)
                } else{
                    like.setImageResource(R.drawable.heart_icon_icons_com_71176)
                }
                if (post.repostByMe){
                    repost?.setImageResource(R.drawable.share_77929)
                } else{
                    repost.setImageResource(R.drawable._share_90177)
                }

            }

            binding.repost.setOnClickListener {
               viewModel.repost()
            }

            binding.like.setOnClickListener {
                viewModel.like()
            }

            binding.autor.setOnClickListener {
                showFullTextDialog(post.author)
            }
        }
    }

    private fun MainActivity.showFullTextDialog(author: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Полный текст")
        builder.setMessage(author)
        builder.setPositiveButton("Закрыть") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun formatnumber(count: Int): String {
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


    private fun applyinset(binding: ActivityMainBinding) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft + systemBars.left,
                v.paddingTop + systemBars.top,
                v.paddingRight + systemBars.right,
                v.paddingBottom + systemBars.bottom
            )
            insets
        }
    }
}


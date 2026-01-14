package ru.netology.nmedia

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyinset(binding)
        val post = Post(
            1L, "Нетология. Университет интернет профессий будущего", "13 января 2026 года",
            "«Нетология» специализируется на переподготовке, высшем образовании (совместно с вузами) и дополнительном обучении специалистов в сферах интернет-маркетинга, управления проектами, дизайна и пользовательского интерфейса, программирования, аналитики и data science, финансов, а также творческих профессий и социальных навыков.",
            false, "5199", false, "5199"
        )
        binding.content.text = post.content
        binding.data.text = post.data
        binding.autor.text = post.author
        binding.likeQuantity.text = formatnumber( post.likeQuantity.toInt())
        binding.repostQuantity.text = formatnumber(post.repostQality.toInt())

        var repostOnClick = false
        binding.repost.setOnClickListener {
            if (!repostOnClick) {
                repostOnClick = true
                binding.repost.setImageResource(R.drawable.share_77929)
                binding.repost.setOnClickListener(null)
                post.repostQality = (post.repostQality.toInt() + 1).toString()
                binding.repostQuantity.text = formatnumber( post.repostQality.toInt())
            }
        }


        binding.like.setOnClickListener {
            post.likedByMe = !post.likedByMe
            binding.like.setImageResource(
                if (post.likedByMe) {
                    R.drawable.like_red
                } else {
                    R.drawable.heart_icon_icons_com_71176
                }
            )

            if (post.likedByMe) {
                post.likeQuantity = (post.likeQuantity.toInt() + 1).toString()


            } else {
                post.likeQuantity = (post.likeQuantity.toInt() - 1).toString()
            }

            binding.likeQuantity.text = formatnumber(post.likeQuantity.toInt())
        }
    }
}

private fun formatnumber(count: Int) : String {
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


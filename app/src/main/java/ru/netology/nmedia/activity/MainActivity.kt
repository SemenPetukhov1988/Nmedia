package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    // Заготовка под биндинг

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left, systemBars.top, systemBars.right, systemBars.bottom
            )
            insets
        }

// делаем нашу вью мождель устойчивой к переворачиванию экрана
        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostContract()) { result ->
            result?.let { newPostContent ->
                // Создание нового поста
                viewModel.save(Post(id = 0,"Netology","11.02.2026",newPostContent, videoVisibility = false))
            }
        }
        val editPostLauncher = registerForActivityResult(EditPostContract()) { result ->
            result?.let { (id, content) ->
                viewModel.updatePost(id!!, content)
            }
        }


        val adapter = PostAdapter(editPostLauncher,object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun OnRepost(post: Post) {
                viewModel.repostById(post.id)
                // отправка текста поста в другие приложения
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                // красивое окошко вызова окна где выбираеш приложение для репоста
                val chooser = Intent.createChooser(intent, "Поделиться")
                startActivity(chooser)
            }

            override fun OnRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
             //   viewModel.edit(post)
            }

            override fun onVideo(video: String) {
               startActivity(
                Intent(Intent.ACTION_VIEW, video.toUri()))
            }

            override fun onShowFulText(post: Post) {
//                showFullText(post.content)
            }

        }

        )
        binding.list?.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list?.scrollToPosition(0)
                }
            }

        }

        binding.add.setOnClickListener {

            newPostLauncher.launch()
        }




    }
}
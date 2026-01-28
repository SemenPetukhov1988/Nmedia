package ru.netology.nmedia

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
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
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Реализация оболочки для поста, чтобы он отслеживался
        val viewModel: PostViewModel by viewModels()

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun OnRepost(post: Post) {
                viewModel.repostById(post.id)
            }

            override fun OnRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onShowFulText(post: Post) {
               showFullText(post.content)
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
        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                val firstWord = viewModel.getFirstWord()
                binding.firstWord?.setText(firstWord)
                binding.canselMenu?.visibility = View.VISIBLE
                binding.editcontent?.setText(post.content)
                AndroidUtils.showKeyboard(binding.root)

            }

        }
        binding.cansel?.setOnClickListener {
            binding.editcontent?.setText("")
            binding.canselMenu?.visibility = View.INVISIBLE
            AndroidUtils.hideKeyboard(binding.root)

        }
        binding.save?.setOnClickListener {
            val text = binding.editcontent?.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this, R.string.Error, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.save(text)
            binding.canselMenu?.visibility = View.INVISIBLE
            binding.editcontent?.setText("")
            binding.editcontent?.clearFocus()
            AndroidUtils.hideKeyboard(binding.root)
        }
    }
    fun showFullText(author: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Полный текст")
        builder.setMessage(author)
        builder.setPositiveButton("Закрыть") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}





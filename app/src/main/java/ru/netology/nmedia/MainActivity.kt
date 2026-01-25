package ru.netology.nmedia

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
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
                v.paddingLeft + systemBars.left,
                v.paddingTop + systemBars.top,
                v.paddingRight + systemBars.right,
                v.paddingBottom + systemBars.bottom
            )
            insets
        }

        // Реализация оболочки для поста, чтобы он отслеживался
        val viewModel: PostViewModel by viewModels()

        val adapter = PostAdapter (
            likeCallback = { post -> viewModel.likeById(post.id) },
            repostCallback = { post -> viewModel.repostById(post.id)},
            activity = this

        )
        binding.postsContainer?.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)

        }
    }
}


// Диалоговое окно для показа полного текста
fun MainActivity.showFullTextDialog(author: String) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Полный текст")
    builder.setMessage(author)
    builder.setPositiveButton("Закрыть") { dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}

// Функция для форматирования числа

package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.util.AndroidUtils

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        Log.d("NewPostActivity", "Save button clicked")
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val receivedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        val postId = intent.getLongExtra("POST_ID", -1L)
        binding.content.setText(receivedText)

        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                    if (postId != -1L) putExtra("POST_ID", postId)

                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

    }
}


class EditPostContract : ActivityResultContract<Intent, Pair<Long?, String>?>() {
    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Long?, String>? {
        if (intent == null || resultCode != Activity.RESULT_OK) return null
        val id = intent.getLongExtra("POST_ID", -1L)
        val content = intent.getStringExtra(Intent.EXTRA_TEXT)
        return if (id != -1L && content != null) Pair(id, content) else null
    }
}
class NewPostContract : ActivityResultContract<Unit, String?>() {

    override fun createIntent(context: Context, input: Unit) =
        Intent(context, NewPostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)
}

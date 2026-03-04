package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.databinding.FragmentOnePostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragment.NewPostFragment.Companion.textArg
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.TextArg
import ru.netology.nmedia.util.TextArg.getValue
import ru.netology.nmedia.util.TextArg.setValue
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.getValue

class OnePostFragment : Fragment() {
    companion object {
        var Bundle.longArg by LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val postId = arguments?.longArg ?: -1
        val viewModel: PostViewModel by activityViewModels()
        val binding = FragmentOnePostBinding.inflate(inflater, container, false)
        val viewHolder = PostViewHolder(binding.post, object : OnInteractionListener {

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
                viewModel.edit(post)
                // код для перехода на новый фрагмент + передача данных
                findNavController().navigate(
                    R.id.newPostFragment,
                    Bundle().apply { textArg = post.content }
                )


            }

            override fun onVideo(video: String) {
                startActivity(
                    Intent(Intent.ACTION_VIEW, video.toUri())
                )
            }

            override fun onShowFulText(post: Post) {
//                showFullText(post.content)
            }

            override fun onOpen(post: Post) {
               // TODO("Not yet implemented")
            }

        })
       viewModel.data.observe(viewLifecycleOwner) { posts ->
           val currentPost = posts.find { it.id == postId }
           if (currentPost == null) {
               // Пост удалён или стал недействительным
               findNavController().navigateUp()
               return@observe
           }
           viewHolder.bind(currentPost)
       }

        return binding.root
    }

}
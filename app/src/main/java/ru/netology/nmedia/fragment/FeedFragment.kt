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
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragment.NewPostFragment.Companion.textArg
import ru.netology.nmedia.fragment.OnePostFragment.Companion.longArg
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(layoutInflater)

        val viewModel: PostViewModel by activityViewModels()

        val adapter = PostAdapter(object : OnInteractionListener {

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
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply { textArg = post.content }
                )
            }

            override fun onVideo(video: String) {
                startActivity(
                    Intent(Intent.ACTION_VIEW, video.toUri())
                )
            }

            override fun onOpen(post: Post) {
                findNavController().navigate(R.id.onePostFragment, Bundle().apply {
                    longArg = post.id
                })
            }
            override fun onShowFulText(post: Post) {
//                showFullText(post.content)
            }

        }

        )
        binding.list?.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list?.scrollToPosition(0)
                }
            }

        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }

}
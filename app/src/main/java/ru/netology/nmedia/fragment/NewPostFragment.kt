package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.TextArg
import ru.netology.nmedia.viewModel.PostViewModel
import ru.netology.nmedia.R
class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg by TextArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: PostViewModel by activityViewModels()

        val binding = FragmentNewPostBinding.inflate(layoutInflater)
        // код для получения данных от другого фрагмента
        arguments?.textArg?.let {
            binding.content.setText(it)
            arguments?.clear()
        }

        binding.save.setOnClickListener {
            if (!binding.content.text.isNullOrBlank()) {
                val content = binding.content.text.toString()
                viewModel.save(content)
            }
            findNavController().navigate(R.id.feedFragment)
        }
        return binding.root
    }


}
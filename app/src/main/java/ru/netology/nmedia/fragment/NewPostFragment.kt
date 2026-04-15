package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.TextArg
import ru.netology.nmedia.viewModel.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.model.SaveModel
import ru.netology.nmedia.util.AndroidUtils

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

            val content = binding.content.text.toString()
            viewModel.save(content)
            AndroidUtils.hideKeyboard(requireView())

        }
        viewModel.dataSave.observe(viewLifecycleOwner) { state ->
            if (state.loading) {
                binding.progressSave.isVisible = true
                binding.content.isEnabled = false
                binding.save.isEnabled = false
            }
        }
        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.load()
            findNavController().navigateUp()
        }
        return binding.root
    }


}
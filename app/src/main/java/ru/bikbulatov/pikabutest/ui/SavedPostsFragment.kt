package ru.bikbulatov.pikabutest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.bikbulatov.pikabutest.R
import ru.bikbulatov.pikabutest.databinding.FragmentSavedPostsBinding
import ru.bikbulatov.pikabutest.db.DbUtils
import ru.bikbulatov.pikabutest.db.PostModel
import ru.bikbulatov.pikabutest.ui.adapters.PostsAdapter

@AndroidEntryPoint
class SavedPostsFragment : Fragment() {
    private lateinit var binding: FragmentSavedPostsBinding
    private lateinit var postsViewModel: PostsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postsViewModel = ViewModelProvider(requireActivity()).get(PostsViewModel::class.java)
        postsViewModel.getSavedPosts()
        observeOnPosts()
    }

    private fun observeOnPosts() {
        postsViewModel.savedPostsList.observe(
            viewLifecycleOwner,
            { posts: MutableList<PostModel>? ->
                if (posts.isNullOrEmpty())
                    binding.tvEmptyList.visibility = View.VISIBLE
                else if (binding.tvEmptyList.visibility == View.VISIBLE)
                    binding.tvEmptyList.visibility = View.GONE
                posts?.let {
                    binding.rvSavedPosts.apply {
                        layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        adapter =
                            PostsAdapter(
                                requireContext(),
                                DbUtils.convertDbToDataClass(posts),
                                parentFragmentManager,
                                postsViewModel,
                                R.id.flSavedPostsFeedRoot
                            )
                    }
                }
            })
    }
}
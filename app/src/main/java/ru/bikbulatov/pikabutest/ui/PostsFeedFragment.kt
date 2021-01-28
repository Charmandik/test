package ru.bikbulatov.pikabutest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.bikbulatov.pikabutest.PostsViewModel
import ru.bikbulatov.pikabutest.R
import ru.bikbulatov.pikabutest.data.models.Post
import ru.bikbulatov.pikabutest.databinding.FragmentPostsFeedBinding
import ru.bikbulatov.pikabutest.db.PostModel
import ru.bikbulatov.pikabutest.helpers.Event
import ru.bikbulatov.pikabutest.helpers.Status
import ru.bikbulatov.pikabutest.ui.adapters.PostsAdapter

@AndroidEntryPoint
class PostsFeedFragment : Fragment() {
    private lateinit var binding: FragmentPostsFeedBinding
    private lateinit var postsViewModel: PostsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postsViewModel = ViewModelProvider(requireActivity()).get(PostsViewModel::class.java)
        postsViewModel.getPosts()
        observeOnPosts()
        observeOnSavedPosts()
    }

    private fun observeOnSavedPosts() {
        postsViewModel.savedPostsList.observe(
            viewLifecycleOwner,
            { posts: MutableList<PostModel>? ->
                posts?.let {
                    binding.rvPosts.adapter?.notifyDataSetChanged()
                }
            })
    }

    private fun observeOnPosts() {
        postsViewModel.postsList.observe(viewLifecycleOwner, { posts: Event<List<Post>>? ->
            when (posts?.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if (posts.data.isNullOrEmpty())
                        binding.tvEmptyList.visibility = View.VISIBLE
                    else if (binding.tvEmptyList.visibility == View.VISIBLE)
                        binding.tvEmptyList.visibility = View.GONE

                    posts.data?.let {
                        binding.rvPosts.apply {
                            layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            adapter =
                                PostsAdapter(
                                    requireContext(),
                                    posts.data,
                                    parentFragmentManager,
                                    postsViewModel,
                                    R.id.flPostsFeedRoot
                                )
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), posts.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
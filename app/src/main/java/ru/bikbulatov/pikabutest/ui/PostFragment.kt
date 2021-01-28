package ru.bikbulatov.pikabutest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.bikbulatov.pikabutest.PostsViewModel
import ru.bikbulatov.pikabutest.data.models.Post
import ru.bikbulatov.pikabutest.databinding.FragmentPostBinding
import ru.bikbulatov.pikabutest.db.DbUtils
import ru.bikbulatov.pikabutest.helpers.Status

class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding
    private lateinit var postsViewModel: PostsViewModel
    private var postId: Int = 0
    private var title: String = ""
    private var body: String? = null
    private var images: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        postsViewModel = ViewModelProvider(requireActivity()).get(PostsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId = requireArguments().getInt("postId")
        title = requireArguments().getString("title", "")
        body = requireArguments().getString("body", "")
        images = requireArguments().getStringArrayList("images")
        initView()
    }

    private fun initView() {
        if (isHaveImages()) {
            postsViewModel.getPost(postId)
            postsViewModel.post.observe(viewLifecycleOwner, {
                when (it?.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        it.data?.let { post: Post ->
                            title = post.title
                            body = post.body
                            images = post.images
                        }
                        loadPost()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.error?.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
        } else
            loadPost()

        configureBtn()
    }

    private fun loadPost() {
        binding.tvTitle.text = title
        binding.tvBody.text = body
        if (!images.isNullOrEmpty())
            Glide
                .with(this)
                .load(images?.first())
                .fitCenter()
                .into(binding.ivBody)
    }

    private fun isHaveImages(): Boolean {
        return images.isNullOrEmpty() && body.isNullOrEmpty()
    }

    private fun configureBtn() {
        binding.btnSave.apply {
            text =
                if (DbUtils.isPostSaved(postId)) "Убрать из сохраненного" else "Сохранить"
            setOnClickListener {
                if (DbUtils.isPostSaved(postId)) {
                    DbUtils.deletePost(postId)
                    text = "Сохранить"
                } else {
                    DbUtils.savePost(postId, title)
                    text = "Убрать из сохраненного"
                }
                postsViewModel.getSavedPosts()
            }
        }
    }
}
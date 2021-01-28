package ru.bikbulatov.pikabutest.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.bikbulatov.pikabutest.ui.PostsViewModel
import ru.bikbulatov.pikabutest.R
import ru.bikbulatov.pikabutest.data.models.Post
import ru.bikbulatov.pikabutest.db.DbUtils
import ru.bikbulatov.pikabutest.ui.PostFragment

class PostsAdapter(
    private val context: Context,
    private val postsList: List<Post>,
    private val fragmentManager: FragmentManager,
    private val postsViewModel: PostsViewModel,
    private val rootId: Int
) :
    RecyclerView.Adapter<PostsAdapter.PostAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapterViewHolder {
        return PostAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostAdapterViewHolder, position: Int) {
        holder.tvTitle.text = postsList[position].title
        holder.btnSave.apply {
            text =
                if (DbUtils.isPostSaved(postsList[position].id)) "Убрать из сохраненного" else "Сохранить"

            setOnClickListener {
                if (DbUtils.isPostSaved(postsList[position].id)) {
                    DbUtils.deletePost(postsList[position].id)
                    text = "Сохранить"
                } else {
                    DbUtils.savePost(postsList[position].id, postsList[position].title)
                    text = "Убрать из сохраненного"
                }
                postsViewModel.getSavedPosts()
            }
        }

        val postFragment = PostFragment()
        val bundle = Bundle().apply {
            putInt("postId", postsList[position].id)
            putString("title", postsList[position].title)
            putString("body", postsList[position].body)
            putStringArrayList("images", postsList[position].images)
        }
        postFragment.arguments = bundle
        holder.llPostRoot.setOnClickListener {
            fragmentManager
                .beginTransaction()
                .replace(rootId, postFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    inner class PostAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llPostRoot: ConstraintLayout = itemView.findViewById(R.id.clPostRoot)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var btnSave: Button = itemView.findViewById(R.id.btnSave)
    }
}
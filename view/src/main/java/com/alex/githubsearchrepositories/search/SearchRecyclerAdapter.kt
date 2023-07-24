package com.alex.githubsearchrepositories.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.databinding.ItemRepoRecyclerBinding
import com.alex.githubsearchrepositories.util.getTime
import inc.alex.data.repo.Repo

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchViewHolder>() {

    private var searchResults: List<Repo> = ArrayList()

    fun updateData(items: List<Repo>) {
        searchResults = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemRepoRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindData(searchResults[holder.absoluteAdapterPosition])
    }
}

class SearchViewHolder(private val binding: ItemRepoRecyclerBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindData(repoEntity: Repo) = with(binding) {
        repoName.text = repoEntity.name
        repoDescription.text = repoEntity.description
        updatedAt.text = itemView.context.let { context ->
            context.getString(
                R.string.updated,
                repoEntity.updatedAt.getTime(context)
            )
        }
        repoLanguage.text = repoEntity.language
        repoWatchers.text = repoEntity.watchers.toString()
    }
}

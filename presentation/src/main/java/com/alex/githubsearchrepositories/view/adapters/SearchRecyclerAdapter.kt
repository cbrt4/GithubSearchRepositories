package com.alex.githubsearchrepositories.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.databinding.ItemRepoRecyclerBinding
import com.alex.githubsearchrepositories.util.DateFormatUtil
import com.example.model.dto.repo.Repo

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {

    var searchResults: List<Repo> = ArrayList()

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

    inner class SearchViewHolder(private val binding: ItemRepoRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(repoEntity: Repo) {
            binding.repoName.text = repoEntity.name
            binding.repoDescription.text = repoEntity.description
            binding.updatedAt.text = itemView.context.getString(
                R.string.updated,
                DateFormatUtil.getTime(repoEntity.updatedAt)
            )
            binding.repoLanguage.text = repoEntity.language
            binding.repoWatchers.text = repoEntity.watchers.toString()
        }
    }
}

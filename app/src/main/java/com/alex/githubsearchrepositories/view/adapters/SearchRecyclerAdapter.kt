package com.alex.githubsearchrepositories.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.dto.repo.Repo
import com.alex.githubsearchrepositories.util.DateFormatUtil
import kotlinx.android.synthetic.main.item_repo_recycler.view.*

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {

    var searchResults: List<Repo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_repo_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindData(searchResults[holder.adapterPosition])
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(repoEntity: Repo) {
            itemView.repoName.text = repoEntity.name
            itemView.repoDescription.text = repoEntity.description
            itemView.updatedAt.text = itemView.context.getString(R.string.updated, DateFormatUtil.getTime(repoEntity.updatedAt))
            itemView.repoLanguage.text = repoEntity.language
            itemView.repoWatchers.text = repoEntity.watchers.toString()
        }
    }
}
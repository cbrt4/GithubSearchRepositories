package com.alex.githubsearchrepositories.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.model.repo.RepoEntity

class SearchRecyclerAdapter: RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {

    var searchResults = ArrayList<RepoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_repo_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val view = holder.itemView

    }

    class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
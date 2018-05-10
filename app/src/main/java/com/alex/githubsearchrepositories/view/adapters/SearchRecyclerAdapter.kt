package com.alex.githubsearchrepositories.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.githubsearchrepositories.R
import com.alex.githubsearchrepositories.model.repo.RepoEntity
import com.alex.githubsearchrepositories.util.DateFormatUtil
import kotlinx.android.synthetic.main.item_repo_recycler.view.*
import javax.inject.Inject

class SearchRecyclerAdapter @Inject constructor(private val appContext: Context) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {

    var searchResults = ArrayList<RepoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_repo_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val view = holder.itemView
        val result = searchResults[holder.adapterPosition]

        view.repoName.text = result.name
        view.repoDescription.text = result.description
        view.updatedAt.text = appContext.getString(R.string.updated, DateFormatUtil.getTime(result.updatedAt))
        view.repoLanguage.text = result.language
        view.repoWatchers.text = result.watchers.toString()
    }

    class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
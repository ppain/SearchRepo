package com.paint.searchrepo.ui.base.repo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.paint.searchrepo.R
import com.paint.searchrepo.data.model.repo.Repo
import com.paint.searchrepo.databinding.AdapterItemRepoBinding

class RepoAdapter(
    private var items: List<Repo>,
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<RepoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterItemRepoBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_item_repo,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], listener)

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(private var binding: AdapterItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Repo, listener: OnItemClickListener?) {
            binding.item = item
            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemClick(layoutPosition) }
            }

            binding.executePendingBindings()
        }
    }

    fun setData(list: List<Repo>) {
        items = list
        notifyDataSetChanged()
    }
}
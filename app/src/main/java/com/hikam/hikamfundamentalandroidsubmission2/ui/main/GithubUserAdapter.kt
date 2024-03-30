@file:Suppress("DEPRECATION")

package com.hikam.hikamfundamentalandroidsubmission2.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.databinding.ItemGithubUserBinding
import jp.wasabeef.glide.transformations.CropCircleTransformation

class GithubUserAdapter(private val userList: List<ItemsItem?>) :
    RecyclerView.Adapter<GithubUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUser.text = userList[position]?.login
        Glide.with(holder.itemView.context)
            .load(userList[position]?.avatarUrl)
            .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
            .into(holder.binding.profileUser)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(userList[holder.adapterPosition])
        }
    }

    override fun getItemCount() = userList.size

    class ViewHolder(val binding: ItemGithubUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem?)
    }
}

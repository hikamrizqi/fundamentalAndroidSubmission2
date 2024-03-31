package com.hikam.hikamfundamentalandroidsubmission2.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.databinding.ActivityFavoriteUserBinding
import com.hikam.hikamfundamentalandroidsubmission2.ui.detail.DetailGithubUserActivity
import com.hikam.hikamfundamentalandroidsubmission2.ui.main.GithubUserAdapter

class FavoriteGithubUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteGithubUserViewModel: FavoriteGithubUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)

        favoriteGithubUserViewModel = getFavoriteViewModel(this@FavoriteGithubUserActivity)
        favoriteGithubUserViewModel.isLoading.observe(this) {
            loading(it)
        }
        favoriteGithubUserViewModel.getAllFavoriteUser().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            val adapter = GithubUserAdapter(items)
            binding.rvFavorite.adapter = adapter
            adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsItem?) {
                    val intent = Intent(applicationContext, DetailGithubUserActivity::class.java)
                    intent.putExtra(DetailGithubUserActivity.DETAIL_USER, data)
                    startActivity(intent)
                }
            })
            loading(false)
        }
    }

    private fun getFavoriteViewModel(activity: AppCompatActivity): FavoriteGithubUserViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteGithubUserViewModel::class.java]
    }

    private fun loading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
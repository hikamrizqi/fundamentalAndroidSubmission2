package com.hikam.hikamfundamentalandroidsubmission2.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hikam.hikamfundamentalandroidsubmission2.R
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.databinding.ActivityMainBinding
import com.hikam.hikamfundamentalandroidsubmission2.ui.detail.DetailGithubUserActivity
import com.hikam.hikamfundamentalandroidsubmission2.ui.favorite.FavoriteGithubUserActivity
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.SettingActivity
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.SettingPreferenceViewModel
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.SettingPreferenceViewModelFactory
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.SettingPreferences
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.dataStore


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBar.inflateMenu(R.menu.option_menu)
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.main_favorite -> {
                    val intent = Intent(this, FavoriteGithubUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.main_theme_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val preference = SettingPreferences.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, SettingPreferenceViewModelFactory(preference))[SettingPreferenceViewModel::class.java]
        preferenceViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        progressBar = findViewById(R.id.progressBar)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(binding.searchView.text)
                    viewModel.searchUsers(searchView.text.toString())
                    searchView.hide()
                    Toast.makeText(this@MainActivity, searchView.text, Toast.LENGTH_SHORT).show()
                    false
                }
        }


        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        binding.rvMain.layoutManager = LinearLayoutManager(this)

        viewModel.userList.observe(this) { itemItem ->
            if (itemItem != null) {
                submitList(itemItem)
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        progressBar.visibility = View.VISIBLE
        viewModel.searchUsers("a")
    }

    private fun submitList(itemsItem: List<ItemsItem?>) {
        val adapter = GithubUserAdapter(itemsItem)
        binding.rvMain.adapter = adapter
        adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem?) {
                val intent = Intent(applicationContext, DetailGithubUserActivity::class.java)
                intent.putExtra(DetailGithubUserActivity.DETAIL_USER, data)
                startActivity(intent)
            }
        })
    }
}

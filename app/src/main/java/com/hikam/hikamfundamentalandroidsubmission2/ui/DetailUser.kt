@file:Suppress("DEPRECATION")

package com.hikam.hikamfundamentalandroidsubmission2.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hikam.hikamfundamentalandroidsubmission2.R
import com.hikam.hikamfundamentalandroidsubmission2.data.response.DetailUserResponse
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.data.retrofit.ApiConfig
import com.hikam.hikamfundamentalandroidsubmission2.database.FavoriteUserEntity
import com.hikam.hikamfundamentalandroidsubmission2.databinding.ActivityDetailUserBinding
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.DetailUserModel
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.FavoriteUserAddUpdateViewModel
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.FavoriteUserViewModel
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.FavoriteUserViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.CropCircleTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailUserModel
    private lateinit var progressBar: ProgressBar
    private lateinit var username: String
    private val TAG_DETAIL_USER = "DetailUser"
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel
    private lateinit var favoriteUser: FavoriteUserEntity
    private lateinit var favoriteUserAddUpdateViewModel: FavoriteUserAddUpdateViewModel
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var favButton: FloatingActionButton

    private var data: ItemsItem? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailUserModel::class.java]
        username = intent.getStringExtra("username") ?: ""
        fetchUserDetails(username)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val progressBar = binding.progressBar

        favButton = binding.favBtn
        favButton.setOnClickListener {
            toggleFavoriteState()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Followers"
            } else {
                tab.text = "Following"
            }
        }.attach()

        data = intent.parcelable(DETAIL_USER)
        favoriteUser = FavoriteUserEntity()

        favoriteUserAddUpdateViewModel = getViewModel(this@DetailUser)
        favoriteUserViewModel = getFavoriteViewModel(this@DetailUser)

        if (data != null) {
            data.let { data ->
                favoriteUser.username = data?.login.toString()
                favoriteUser.avatarUrl = data?.avatarUrl
            }

            favoriteUserViewModel.getFavoriteUsername(data?.login.toString()).observe(this) { favoriteList ->
                isFavorite = favoriteList != null

                if (favoriteList == null) {
                    binding.favBtn.setImageDrawable(resources.getDrawable(R.drawable.baseline_favorite_border_24, null))
                } else {
                    binding.favBtn.setImageDrawable(resources.getDrawable(R.drawable.baseline_favorite_24, null))
                }
            }
        }

        sectionsPagerAdapter.username = data?.login.toString()
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailUserModel::class.java]
        data?.login?.let { detailViewModel.searchUserDetail(it) }
        detailViewModel.detailUser.observe(this) { detailUser ->
            setItemsData(detailUser)
        }
        detailViewModel.isLoading.observe(this) { isLoading ->
            if (!isLoading) {
                progressBar.visibility = View.GONE
            }
        }

        val shareButton = binding.shareBtn
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareMessage = getString(R.string.share_message, username) // Replace with your desired message
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun setItemsData(detailUser: DetailUserResponse?) {
        binding.nameDetail.text = detailUser?.name
        binding.usernameDetail.text = detailUser?.login
        binding.numFollower.text = detailUser?.followers.toString()
        binding.numFollowing.text = detailUser?.following.toString()
        Glide.with(this)
            .load(detailUser?.avatarUrl)
            .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
            .into(binding.detailProfile)
    }

    private fun fetchUserDetails(username: String) {
        detailViewModel.searchUserDetail(username)

        val apiService = ApiConfig.getApiService()
        apiService.getDetailUser(username).enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    val userDetail = response.body()
                    if (userDetail != null) {
                        setItemsData(userDetail) // Update UI with user details
                    }
                } else {
                    val errorMessage = "API error: ${response.code()}"
                    Log.e(TAG_DETAIL_USER, errorMessage)
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                // Handle network failure
                val errorMessage = "Network failure: ${t.message}"
                Log.e(TAG_DETAIL_USER, errorMessage)
            }
        })
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private fun getViewModel(activity: AppCompatActivity): FavoriteUserAddUpdateViewModel {
        val factory = FavoriteUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserAddUpdateViewModel::class.java]
    }

    private fun getFavoriteViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = FavoriteUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    private fun toggleFavoriteState() {
        if (!isFavorite) {
            favoriteUserAddUpdateViewModel.insert(favoriteUser)
            showToast(getString(R.string.addFav))
            isFavorite = true
        } else {
            favoriteUserAddUpdateViewModel.delete(favoriteUser)
            showToast(getString(R.string.removeFav))
            isFavorite = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val DETAIL_USER = "detail_user"
    }
}
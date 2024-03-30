@file:Suppress("DEPRECATION")

package com.hikam.hikamfundamentalandroidsubmission2.ui.detail

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
import com.hikam.hikamfundamentalandroidsubmission2.data.response.DetailGithubUserResponse
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.data.retrofit.ApiConfig
import com.hikam.hikamfundamentalandroidsubmission2.data.helper.FavoriteGithubUserEntity
import com.hikam.hikamfundamentalandroidsubmission2.databinding.ActivityDetailUserBinding
import com.hikam.hikamfundamentalandroidsubmission2.ui.favorite.FavoriteAddUpdateViewModel
import com.hikam.hikamfundamentalandroidsubmission2.ui.favorite.FavoriteGithubUserViewModel
import com.hikam.hikamfundamentalandroidsubmission2.ui.favorite.FavoriteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.CropCircleTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var favoriteGithubUserViewModel: FavoriteGithubUserViewModel
    private lateinit var favoriteUser: FavoriteGithubUserEntity
    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel
    private lateinit var username: String
    private val TAG_GITHUB_DETAIL_USER = "DetailGithubUser"
    private lateinit var favButton: FloatingActionButton
    private lateinit var detailViewModel: DetailUserModel
    private var data: ItemsItem? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailUserModel::class.java]
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
        favoriteUser = FavoriteGithubUserEntity()

        favoriteAddUpdateViewModel = getViewModel(this@DetailUser)
        favoriteGithubUserViewModel = getFavoriteViewModel(this@DetailUser)

        if (data != null) {
            data.let { data ->
                favoriteUser.username = data?.login.toString()
                favoriteUser.avatarUrl = data?.avatarUrl
            }

            favoriteGithubUserViewModel.getFavoriteUsername(data?.login.toString())
                .observe(this) { favoriteList ->
                    isFavorite = favoriteList != null

                    if (favoriteList == null) {
                        binding.favBtn.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.iv_favorite_border,
                                null
                            )
                        )
                    } else {
                        binding.favBtn.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.iv_favorite,
                                null
                            )
                        )
                    }
                }
        }

        sectionsPagerAdapter.username = data?.login.toString()
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailUserModel::class.java]
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
            val shareMessage =
                getString(R.string.share_message, username) // Replace with your desired message
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
        }
    }

    private fun fetchUserDetails(username: String) {
        detailViewModel.searchUserDetail(username)

        val apiService = ApiConfig.getApiService()
        apiService.getDetailUser(username).enqueue(object : Callback<DetailGithubUserResponse> {
            override fun onResponse(
                call: Call<DetailGithubUserResponse>,
                response: Response<DetailGithubUserResponse>
            ) {
                if (response.isSuccessful) {
                    val userDetail = response.body()
                    if (userDetail != null) {
                        setItemsData(userDetail) // Update UI with user details
                    }
                } else {
                    val errorMessage = "API error: ${response.code()}"
                    Log.e(TAG_GITHUB_DETAIL_USER, errorMessage)
                }
            }

            override fun onFailure(call: Call<DetailGithubUserResponse>, t: Throwable) {
                // Handle network failure
                val errorMessage = "Network failure: ${t.message}"
                Log.e(TAG_GITHUB_DETAIL_USER, errorMessage)
            }
        })
    }

    @SuppressLint("StringFormatInvalid")
    private fun setItemsData(detailUser: DetailGithubUserResponse?) {
        binding.nameDetail.text = detailUser?.name
        binding.usernameDetail.text = detailUser?.login
        binding.jumlahFollower.text = detailUser?.followers.toString()
        binding.jumlahFollowing.text = detailUser?.following.toString()
        Glide.with(this)
            .load(detailUser?.avatarUrl)
            .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
            .into(binding.detailProfile)
    }


    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private fun getViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteAddUpdateViewModel::class.java]
    }

    private fun getFavoriteViewModel(activity: AppCompatActivity): FavoriteGithubUserViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteGithubUserViewModel::class.java]
    }

    private fun toggleFavoriteState() {
        if (!isFavorite) {
            favoriteAddUpdateViewModel.insert(favoriteUser)
            showToast(getString(R.string.addFav))
            isFavorite = true
        } else {
            favoriteAddUpdateViewModel.delete(favoriteUser)
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
package com.hikam.hikamfundamentalandroidsubmission2.ui.follow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hikam.hikamfundamentalandroidsubmission2.data.response.ItemsItem
import com.hikam.hikamfundamentalandroidsubmission2.databinding.FragmentFollowBinding
import com.hikam.hikamfundamentalandroidsubmission2.ui.detail.DetailUser
import com.hikam.hikamfundamentalandroidsubmission2.ui.main.GithubUserAdapter

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var followerViewModel: FollowerViewModel
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    private var position: Int = 0
    private var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFollow.layoutManager = LinearLayoutManager(requireContext())

        followerViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowerViewModel::class.java]
        followingViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]

        progressBar = binding.progressBar

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: ""
        }
        if (position == 1){
            followerViewModel.printFollowers(username)
            followerViewModel.usersFollower.observe(viewLifecycleOwner) { usersFollower ->
                submitList(usersFollower)
            }
            followerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        } else {
            followingViewModel.printFollowings(username)
            followingViewModel.usersFollowing.observe(viewLifecycleOwner) { usersFollowing ->
                submitList(usersFollowing)
            }
            followingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun submitList(itemsItem: List<ItemsItem?>) {
        val adapter = GithubUserAdapter(itemsItem)
        binding.rvFollow.adapter = adapter
        adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem?) {
                val intent = Intent(requireActivity(), DetailUser::class.java)
                intent.putExtra(DetailUser.DETAIL_USER, data)
                startActivity(intent)
            }
        })
    }
}
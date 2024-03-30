package com.hikam.hikamfundamentalandroidsubmission2.ui

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.hikam.hikamfundamentalandroidsubmission2.databinding.ActivitySettingBinding
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.SettingPreferenceViewModel
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.SettingPreferenceViewModelFactory
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.SettingPreferences
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.dataStore

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingPreferenceViewModel =
            ViewModelProvider(this, SettingPreferenceViewModelFactory(pref)).get(
                SettingPreferenceViewModel::class.java
            )
        settingPreferenceViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingPreferenceViewModel.keepThemeSetting(isChecked)
        }
    }
}
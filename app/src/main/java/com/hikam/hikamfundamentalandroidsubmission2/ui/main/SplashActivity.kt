package com.hikam.hikamfundamentalandroidsubmission2.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.hikam.hikamfundamentalandroidsubmission2.R
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.SettingPreferenceViewModel
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.SettingPreferenceViewModelFactory
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.SettingPreferences
import com.hikam.hikamfundamentalandroidsubmission2.ui.setting.dataStore

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val delay: Long = 2500
        val intent = Intent(this, MainActivity::class.java)

        Thread {
            Thread.sleep(delay)
            startActivity(intent)
            finish()
        }.start()

        val preference = SettingPreferences.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, SettingPreferenceViewModelFactory(preference))[SettingPreferenceViewModel::class.java]
        preferenceViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
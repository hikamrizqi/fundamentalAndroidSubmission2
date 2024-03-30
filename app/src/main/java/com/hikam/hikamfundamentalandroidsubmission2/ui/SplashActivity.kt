package com.hikam.hikamfundamentalandroidsubmission2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.hikam.hikamfundamentalandroidsubmission2.R
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.SettingPreferenceViewModel
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.SettingPreferenceViewModelFactory
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.SettingPreferences
import com.hikam.hikamfundamentalandroidsubmission2.viewmodel.dataStore

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val delayMillis: Long = 2000
        val mainIntent = Intent(this, MainActivity::class.java)

        Thread {
            Thread.sleep(delayMillis)
            startActivity(mainIntent)
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
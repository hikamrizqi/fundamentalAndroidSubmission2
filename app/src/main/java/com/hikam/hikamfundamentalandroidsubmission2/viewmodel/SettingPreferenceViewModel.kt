package com.hikam.hikamfundamentalandroidsubmission2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingPreferenceViewModel (private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun keepThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.keepThemeSetting(isDarkModeActive)
        }
    }
}
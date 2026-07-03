package com.fishing.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FishingViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FishingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FishingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.summersama.fisimili.ui.searchall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summersama.fisimili.data.AllSongRepository

class AllSongModelFactory(private val repository: AllSongRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AllSongViewModel(repository) as T
    }
}
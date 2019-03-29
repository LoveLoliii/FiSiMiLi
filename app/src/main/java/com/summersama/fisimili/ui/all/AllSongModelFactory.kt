package com.summersama.fisimili.ui.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.summersama.fisimili.data.AllSongRepository
import com.summersama.fisimili.data.SearchRepository

class AllSongModelFactory(private val repository: AllSongRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AllSongViewModel(repository) as T
    }
}
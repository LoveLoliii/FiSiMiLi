package com.summersama.fisimili.ui.songdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summersama.fisimili.data.SongDetailRepository
import com.summersama.fisimili.ui.search.SearchViewModel

class SongDetailFactory(private val repository: SongDetailRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SongDetailViewModel(repository) as T
    }
}

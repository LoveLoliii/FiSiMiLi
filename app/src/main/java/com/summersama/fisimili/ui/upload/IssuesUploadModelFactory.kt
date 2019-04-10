package com.summersama.fisimili.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summersama.fisimili.data.IssuesUploadRepository

class IssuesUploadModelFactory(private val issuesUploadRepository: IssuesUploadRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IssuesUploadViewModel(issuesUploadRepository) as T
    }
}

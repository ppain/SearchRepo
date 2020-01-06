package com.paint.searchrepo.util

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class DebouncingQueryTextListener(
    lifecycleOwner: LifecycleOwner,
    private val onDebouncingQueryTextChange: (String?) -> Unit
) : SearchView.OnQueryTextListener {
    private var debouncePeriod: Long = 500

    private val coroutineScope = lifecycleOwner.lifecycleScope

    private var searchJob: Job? = null

    override fun onQueryTextSubmit(newText: String?): Boolean {
        onDebouncingQueryTextChange(newText)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(debouncePeriod)
                onDebouncingQueryTextChange(newText)
            }
        }
        return false
    }
}
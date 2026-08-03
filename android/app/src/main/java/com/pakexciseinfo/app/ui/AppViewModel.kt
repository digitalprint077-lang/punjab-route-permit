package com.pakexciseinfo.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pakexciseinfo.app.data.AppConfigRepository
import com.pakexciseinfo.app.data.AppContent
import com.pakexciseinfo.app.data.ContentSnapshot
import com.pakexciseinfo.app.util.LinkOpener
import com.pakexciseinfo.app.util.OpenLinkResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val configRepository = AppConfigRepository(application.applicationContext)

    private val _content = MutableStateFlow(AppContent.snapshot())
    val content: StateFlow<ContentSnapshot> = _content.asStateFlow()

    private val _messages = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val messages: SharedFlow<Int> = _messages.asSharedFlow()

    private val _opening = MutableStateFlow(false)
    val opening: StateFlow<Boolean> = _opening.asStateFlow()

    init {
        refreshConfig()
    }

    fun refreshConfig() {
        viewModelScope.launch {
            val config = configRepository.load()
            _content.value = AppContent.snapshot(config)
            _messages.emit(com.pakexciseinfo.app.R.string.refresh_links_done)
        }
    }

    fun openUrl(url: String) {
        viewModelScope.launch {
            _opening.value = true
            val result = LinkOpener.open(getApplication(), url)
            _opening.value = false
            when (result) {
                OpenLinkResult.Opened -> Unit
                OpenLinkResult.Offline -> _messages.emit(com.pakexciseinfo.app.R.string.error_offline)
                OpenLinkResult.InvalidUrl -> _messages.emit(com.pakexciseinfo.app.R.string.error_invalid_url)
                OpenLinkResult.NoApp -> _messages.emit(com.pakexciseinfo.app.R.string.error_no_browser)
            }
        }
    }
}

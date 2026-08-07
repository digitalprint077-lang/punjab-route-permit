package com.vehiclehubpk.app.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vehiclehubpk.app.data.AppConfigRepository
import com.vehiclehubpk.app.data.AppContent
import com.vehiclehubpk.app.data.ContentSnapshot
import com.vehiclehubpk.app.data.PortalInfo
import com.vehiclehubpk.app.util.LinkOpener
import com.vehiclehubpk.app.util.OpenLinkResult
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

    private val _portalInfo = MutableStateFlow<PortalInfo?>(null)
    val portalInfo: StateFlow<PortalInfo?> = _portalInfo.asStateFlow()

    private val _messages = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val messages: SharedFlow<Int> = _messages.asSharedFlow()

    private val _opening = MutableStateFlow(false)
    val opening: StateFlow<Boolean> = _opening.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                val config = configRepository.load()
                _content.value = AppContent.snapshot(config)
            }
        }
    }

    fun preparePortalInfo(info: PortalInfo) {
        _portalInfo.value = info
    }

    fun clearPortalInfo() {
        _portalInfo.value = null
    }

    fun refreshConfig() {
        viewModelScope.launch {
            runCatching {
                val config = configRepository.load()
                _content.value = AppContent.snapshot(config)
                _messages.emit(com.vehiclehubpk.app.R.string.refresh_links_done)
            }.onFailure {
                _messages.emit(com.vehiclehubpk.app.R.string.error_offline)
            }
        }
    }

    /** Opens URL in Chrome Custom Tabs / external browser — never WebView. */
    fun openUrl(context: Context, url: String) {
        viewModelScope.launch {
            _opening.value = true
            val launchContext = if (context is android.app.Activity) context else context.applicationContext
            val result = runCatching {
                LinkOpener.open(launchContext, url)
            }.getOrDefault(OpenLinkResult.NoApp)
            _opening.value = false
            when (result) {
                OpenLinkResult.Opened -> Unit
                OpenLinkResult.Offline -> _messages.emit(com.vehiclehubpk.app.R.string.error_offline)
                OpenLinkResult.InvalidUrl -> _messages.emit(com.vehiclehubpk.app.R.string.error_invalid_url)
                OpenLinkResult.NoApp -> _messages.emit(com.vehiclehubpk.app.R.string.error_no_browser)
            }
        }
    }
}

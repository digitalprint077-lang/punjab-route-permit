package com.pakexciseinfo.app

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.pakexciseinfo.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastErrorUrl: String? = null
    private var splashHidden = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        applyInsets()
        setupWebView()
        setupControls()
        handleBackPress()

        val startUrl = resolveStartUrl(intent) ?: BuildConfig.SITE_URL
        if (savedInstanceState != null) {
            binding.webView.restoreState(savedInstanceState)
            hideSplash()
        } else {
            loadUrl(startUrl)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveStartUrl(intent)?.let { loadUrl(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                loadUrl(BuildConfig.SITE_URL)
                true
            }
            R.id.action_refresh -> {
                reloadCurrent()
                true
            }
            R.id.action_share -> {
                shareCurrentPage()
                true
            }
            R.id.action_browser -> {
                openExternally(currentPageUrl())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updatePadding(top = bars.top)
            binding.swipeRefresh.updatePadding(bottom = bars.bottom)
            binding.errorPanel.updatePadding(bottom = bars.bottom)
            insets
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(binding.webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            cacheMode = WebSettings.LOAD_DEFAULT
            mediaPlaybackRequiresUserGesture = true
            setSupportMultipleWindows(false)
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                if (!title.isNullOrBlank() && !title.equals("about:blank", ignoreCase = true)) {
                    binding.toolbar.subtitle = title
                }
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val uri = request?.url ?: return false
                return handleNavigation(uri)
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val uri = Uri.parse(url ?: return false)
                return handleNavigation(uri)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showLoading(true)
                showError(false)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                hideSplash()
                if (!url.isNullOrBlank() && !url.startsWith("about:")) {
                    binding.toolbar.subtitle = null
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                if (request?.isForMainFrame == true) {
                    lastErrorUrl = request.url?.toString() ?: currentPageUrl()
                    showError(true)
                    showLoading(false)
                    binding.swipeRefresh.isRefreshing = false
                    hideSplash()
                }
            }
        }
    }

    private fun setupControls() {
        binding.swipeRefresh.setColorSchemeResources(R.color.brand_teal, R.color.brand_gold)
        binding.swipeRefresh.setOnRefreshListener { reloadCurrent() }
        binding.retryButton.setOnClickListener {
            val target = lastErrorUrl ?: currentPageUrl()
            loadUrl(target)
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    private fun handleNavigation(uri: Uri): Boolean {
        val scheme = uri.scheme?.lowercase().orEmpty()
        if (scheme == "tel" || scheme == "mailto" || scheme == "sms" || scheme == "geo") {
            openExternally(uri.toString())
            return true
        }
        if (scheme != "http" && scheme != "https") {
            openExternally(uri.toString())
            return true
        }
        val host = uri.host?.lowercase().orEmpty()
        if (isAppHost(host)) {
            return false
        }
        openExternally(uri.toString())
        return true
    }

    private fun isAppHost(host: String): Boolean {
        val siteHost = BuildConfig.SITE_HOST.lowercase()
        return host == siteHost || host == "www.$siteHost"
    }

    private fun loadUrl(url: String) {
        if (!isOnline()) {
            lastErrorUrl = url
            showError(true)
            showLoading(false)
            hideSplash()
            return
        }
        showError(false)
        showLoading(true)
        binding.webView.loadUrl(url)
    }

    private fun reloadCurrent() {
        if (!isOnline()) {
            lastErrorUrl = currentPageUrl()
            showError(true)
            binding.swipeRefresh.isRefreshing = false
            return
        }
        showError(false)
        if (binding.webView.url.isNullOrBlank() || binding.webView.url == "about:blank") {
            loadUrl(lastErrorUrl ?: BuildConfig.SITE_URL)
        } else {
            binding.webView.reload()
        }
    }

    private fun currentPageUrl(): String {
        val url = binding.webView.url
        return if (!url.isNullOrBlank() && URLUtil.isNetworkUrl(url)) url else BuildConfig.SITE_URL
    }

    private fun resolveStartUrl(intent: Intent?): String? {
        val data = intent?.data ?: return null
        return if (isAppHost(data.host?.lowercase().orEmpty())) data.toString() else null
    }

    private fun shareCurrentPage() {
        val share = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            putExtra(Intent.EXTRA_TEXT, currentPageUrl())
        }
        startActivity(Intent.createChooser(share, getString(R.string.menu_share)))
    }

    private fun openExternally(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, R.string.error_title, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }

    private fun showError(show: Boolean) {
        binding.errorPanel.visibility = if (show) View.VISIBLE else View.GONE
        binding.swipeRefresh.visibility = if (show) View.INVISIBLE else View.VISIBLE
    }

    private fun hideSplash() {
        if (splashHidden) return
        splashHidden = true
        binding.splash.animate()
            .alpha(0f)
            .setDuration(280L)
            .withEndAction { binding.splash.visibility = View.GONE }
            .start()
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(ConnectivityManager::class.java) ?: return true
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

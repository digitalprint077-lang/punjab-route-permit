# Keep WebView bridge classes if JS interfaces are added later.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

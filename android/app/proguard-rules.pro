# Keep optional Crashlytics reflection bridge.
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Keep Compose / navigation
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

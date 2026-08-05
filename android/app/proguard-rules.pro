# App code (needed if minify is turned back on)
-keep class com.vehiclehubpk.app.** { *; }
-keepclassmembers class com.vehiclehubpk.app.** { *; }

# Compose
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn androidx.compose.**

# Navigation
-keep class androidx.navigation.** { *; }

# Coroutines
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# Optional Crashlytics reflection bridge
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

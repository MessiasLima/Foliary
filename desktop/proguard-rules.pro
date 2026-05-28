# Room
-keep class * extends androidx.room3.RoomDatabase { <init>(); }

# Coroutines
-keep class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }

# Ktor
-keep class io.ktor.client.engine.** { *; }
-keep class io.ktor.serialization.kotlinx.json.** { *; }

# ComposeNativeTray
-keep class com.sun.jna.** { *; }
-keep class com.kdroid.composetray.** { *; }

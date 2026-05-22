# Room
-keep class * extends androidx.room.RoomDatabase { <init>(); }

# Coroutines
-keep class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }

# Ktor
-keep class io.ktor.client.engine.** { *; }
-keep class io.ktor.serialization.kotlinx.json.** { *; }

-dontwarn **

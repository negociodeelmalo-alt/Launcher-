package com.example.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.example.data.model.AppCategory
import com.example.data.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager
    private val currentPackageName: String = context.packageName

    suspend fun getInstalledApps(favoritePackages: Set<String>): List<AppInfo> = withContext(Dispatchers.IO) {
        val appMap = mutableMapOf<String, AppInfo>()

        // 1. Query Leanback Launcher apps (Android TV specific)
        try {
            val tvIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
            }
            val tvResolveInfos = packageManager.queryIntentActivities(tvIntent, 0)
            for (resolveInfo in tvResolveInfos) {
                val pkgName = resolveInfo.activityInfo.packageName
                if (pkgName == currentPackageName) continue

                val appName = resolveInfo.loadLabel(packageManager)?.toString() ?: pkgName
                val icon = resolveInfo.loadIcon(packageManager)
                val isSystem = (resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

                val category = classifyApp(pkgName, appName)

                appMap[pkgName] = AppInfo(
                    id = pkgName,
                    name = appName,
                    packageName = pkgName,
                    activityName = resolveInfo.activityInfo.name,
                    icon = icon,
                    isFavorite = favoritePackages.contains(pkgName),
                    isSystemApp = isSystem,
                    category = category
                )
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error querying Leanback apps", e)
        }

        // 2. Query Standard Launcher apps (to ensure all installed apps appear)
        try {
            val standardIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val standardResolveInfos = packageManager.queryIntentActivities(standardIntent, 0)
            for (resolveInfo in standardResolveInfos) {
                val pkgName = resolveInfo.activityInfo.packageName
                if (pkgName == currentPackageName) continue

                // Don't overwrite if leanback activity info is already present
                if (!appMap.containsKey(pkgName)) {
                    val appName = resolveInfo.loadLabel(packageManager)?.toString() ?: pkgName
                    val icon = resolveInfo.loadIcon(packageManager)
                    val isSystem = (resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    val category = classifyApp(pkgName, appName)

                    appMap[pkgName] = AppInfo(
                        id = pkgName,
                        name = appName,
                        packageName = pkgName,
                        activityName = resolveInfo.activityInfo.name,
                        icon = icon,
                        isFavorite = favoritePackages.contains(pkgName),
                        isSystemApp = isSystem,
                        category = category
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error querying standard apps", e)
        }

        // Return sorted list: favorites first, then alphabetically by name
        appMap.values.sortedWith(
            compareByDescending<AppInfo> { it.isFavorite }
                .thenBy { it.name.lowercase() }
        )
    }

    private fun classifyApp(packageName: String, appName: String): AppCategory {
        val lowerPkg = packageName.lowercase()
        val lowerName = appName.lowercase()

        val mediaKeywords = listOf(
            "youtube", "netflix", "prime", "disney", "hbo", "max", "plex", "kodi",
            "vlc", "spotify", "music", "video", "tv", "twitch", "player", "movie",
            "cinema", "crunchyroll", "audio", "radio", "gallery"
        )
        val toolKeywords = listOf(
            "setting", "ajuste", "configur", "file", "archivo", "explorer",
            "browser", "navegador", "chrome", "download", "descarga", "calc",
            "terminal", "cleaner", "device", "info", "system"
        )

        return when {
            mediaKeywords.any { lowerPkg.contains(it) || lowerName.contains(it) } -> AppCategory.MEDIA
            toolKeywords.any { lowerPkg.contains(it) || lowerName.contains(it) } -> AppCategory.TOOLS
            else -> AppCategory.ALL
        }
    }

    fun launchApp(app: AppInfo): Boolean {
        return try {
            // First try specific launch intent for package
            val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)
                ?: Intent(Intent.ACTION_MAIN).apply {
                    if (app.activityName != null) {
                        setClassName(app.packageName, app.activityName)
                    } else {
                        addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
                        `package` = app.packageName
                    }
                }

            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            context.startActivity(launchIntent)
            true
        } catch (e: ActivityNotFoundException) {
            Log.e("AppRepository", "Activity not found for package ${app.packageName}", e)
            false
        } catch (e: Exception) {
            Log.e("AppRepository", "Failed to launch ${app.packageName}", e)
            false
        }
    }

    fun openAppDetails(packageName: String) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("AppRepository", "Failed to open app details for $packageName", e)
        }
    }

    fun requestUninstall(packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_DELETE).apply {
                data = Uri.fromParts("package", packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("AppRepository", "Failed to request uninstall for $packageName", e)
        }
    }

    fun openSystemSettings() {
        try {
            val tvSettingsIntent = Intent("android.settings.TV_SETTINGS").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(tvSettingsIntent)
        } catch (e: Exception) {
            try {
                val standardSettingsIntent = Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(standardSettingsIntent)
            } catch (e2: Exception) {
                Log.e("AppRepository", "Failed to open settings", e2)
            }
        }
    }
}

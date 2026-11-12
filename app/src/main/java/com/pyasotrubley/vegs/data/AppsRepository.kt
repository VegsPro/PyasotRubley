package com.pyasotrubley.vegs.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppsRepository(private val context: Context) {

    suspend fun loadLaunchableApps(hideSystem: Boolean = false): List<AppInfo> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos: List<ResolveInfo> = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)

        resolveInfos
            .mapNotNull { ri ->
                val activityInfo = ri.activityInfo ?: return@mapNotNull null
                val appInfo = activityInfo.applicationInfo
                val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                if (hideSystem && isSystem) return@mapNotNull null

                val label = ri.loadLabel(pm)?.toString() ?: activityInfo.packageName
                val icon = try {
                    ri.loadIcon(pm)
                } catch (e: Exception) {
                    null
                }

                AppInfo(
                    packageName = activityInfo.packageName,
                    activityName = activityInfo.name,
                    label = label,
                    icon = icon,
                    isSystem = isSystem
                )
            }
            .distinctBy { it.uniqueId }
            .sortedBy { it.label.lowercase() }
    }

    fun launchApp(app: AppInfo) {
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(app.packageName, app.activityName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

package com.pyasotrubley.vegs.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val GLASS = booleanPreferencesKey("glass_enabled")
        val HOME_COLUMNS = intPreferencesKey("home_columns")
        val ICON_SIZE = intPreferencesKey("icon_size_dp")
        val SHOW_LABELS = booleanPreferencesKey("show_labels")
        val DOCK_COLUMNS = intPreferencesKey("dock_columns")
        val LABEL_SIZE = intPreferencesKey("label_size_sp")
        val PAGE_INDICATOR = booleanPreferencesKey("page_indicator")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val BLUR_STRENGTH = floatPreferencesKey("blur_strength")
        val ICON_CORNER = intPreferencesKey("icon_corner_radius")
        val SEARCH_DRAWER = booleanPreferencesKey("search_in_drawer")
        val HIDE_SYSTEM = booleanPreferencesKey("hide_system_apps")
        val DOUBLE_TAP_LOCK = booleanPreferencesKey("double_tap_lock")
        val SWIPE_UP_DRAWER = booleanPreferencesKey("swipe_up_drawer")
    }

    val settings: Flow<LauncherSettings> = context.dataStore.data.map { prefs ->
        LauncherSettings(
            glassEnabled = prefs[Keys.GLASS] ?: false,
            homeColumns = prefs[Keys.HOME_COLUMNS] ?: 4,
            iconSizeDp = prefs[Keys.ICON_SIZE] ?: 56,
            showLabels = prefs[Keys.SHOW_LABELS] ?: true,
            dockColumns = prefs[Keys.DOCK_COLUMNS] ?: 5,
            labelSizeSp = prefs[Keys.LABEL_SIZE] ?: 12,
            pageIndicator = prefs[Keys.PAGE_INDICATOR] ?: true,
            darkTheme = prefs[Keys.DARK_THEME] ?: true,
            blurStrength = prefs[Keys.BLUR_STRENGTH] ?: 20f,
            iconCornerRadius = prefs[Keys.ICON_CORNER] ?: 16,
            searchInDrawer = prefs[Keys.SEARCH_DRAWER] ?: true,
            hideSystemApps = prefs[Keys.HIDE_SYSTEM] ?: false,
            doubleTapLock = prefs[Keys.DOUBLE_TAP_LOCK] ?: false,
            swipeUpDrawer = prefs[Keys.SWIPE_UP_DRAWER] ?: true
        )
    }

    suspend fun update(transform: (LauncherSettings) -> LauncherSettings) {
        context.dataStore.edit { prefs ->
            val current = LauncherSettings(
                glassEnabled = prefs[Keys.GLASS] ?: false,
                homeColumns = prefs[Keys.HOME_COLUMNS] ?: 4,
                iconSizeDp = prefs[Keys.ICON_SIZE] ?: 56,
                showLabels = prefs[Keys.SHOW_LABELS] ?: true,
                dockColumns = prefs[Keys.DOCK_COLUMNS] ?: 5,
                labelSizeSp = prefs[Keys.LABEL_SIZE] ?: 12,
                pageIndicator = prefs[Keys.PAGE_INDICATOR] ?: true,
                darkTheme = prefs[Keys.DARK_THEME] ?: true,
                blurStrength = prefs[Keys.BLUR_STRENGTH] ?: 20f,
                iconCornerRadius = prefs[Keys.ICON_CORNER] ?: 16,
                searchInDrawer = prefs[Keys.SEARCH_DRAWER] ?: true,
                hideSystemApps = prefs[Keys.HIDE_SYSTEM] ?: false,
                doubleTapLock = prefs[Keys.DOUBLE_TAP_LOCK] ?: false,
                swipeUpDrawer = prefs[Keys.SWIPE_UP_DRAWER] ?: true
            )
            val updated = transform(current)
            prefs[Keys.GLASS] = updated.glassEnabled
            prefs[Keys.HOME_COLUMNS] = updated.homeColumns
            prefs[Keys.ICON_SIZE] = updated.iconSizeDp
            prefs[Keys.SHOW_LABELS] = updated.showLabels
            prefs[Keys.DOCK_COLUMNS] = updated.dockColumns
            prefs[Keys.LABEL_SIZE] = updated.labelSizeSp
            prefs[Keys.PAGE_INDICATOR] = updated.pageIndicator
            prefs[Keys.DARK_THEME] = updated.darkTheme
            prefs[Keys.BLUR_STRENGTH] = updated.blurStrength
            prefs[Keys.ICON_CORNER] = updated.iconCornerRadius
            prefs[Keys.SEARCH_DRAWER] = updated.searchInDrawer
            prefs[Keys.HIDE_SYSTEM] = updated.hideSystemApps
            prefs[Keys.DOUBLE_TAP_LOCK] = updated.doubleTapLock
            prefs[Keys.SWIPE_UP_DRAWER] = updated.swipeUpDrawer
        }
    }
}

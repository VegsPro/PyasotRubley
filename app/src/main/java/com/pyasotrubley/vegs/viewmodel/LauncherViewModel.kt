package com.pyasotrubley.vegs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pyasotrubley.vegs.PyasotRubleyApp
import com.pyasotrubley.vegs.data.AppInfo
import com.pyasotrubley.vegs.data.AppsRepository
import com.pyasotrubley.vegs.data.LauncherSettings
import com.pyasotrubley.vegs.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepo: SettingsRepository = (application as PyasotRubleyApp).settingsRepository
    private val appsRepo = AppsRepository(application)

    val settings: StateFlow<LauncherSettings> = settingsRepo.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, LauncherSettings())

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredApps: StateFlow<List<AppInfo>> = combine(_allApps, _searchQuery, settings) { apps, query, s ->
        val filtered = if (s.hideSystemApps) apps.filter { !it.isSystem } else apps
        if (query.isBlank()) filtered
        else filtered.filter { it.label.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Home screen items (simple for now: first N apps as "pinned")
    private val _homeApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val homeApps: StateFlow<List<AppInfo>> = _homeApps.asStateFlow()

    private val _dockApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val dockApps: StateFlow<List<AppInfo>> = _dockApps.asStateFlow()

    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    init {
        refreshApps()
    }

    fun refreshApps() {
        viewModelScope.launch {
            val apps = appsRepo.loadLaunchableApps(settings.value.hideSystemApps)
            _allApps.value = apps
            // Simple defaults for home/dock
            if (_homeApps.value.isEmpty() && apps.isNotEmpty()) {
                _homeApps.value = apps.take(12)
            }
            if (_dockApps.value.isEmpty() && apps.isNotEmpty()) {
                _dockApps.value = apps.take(5)
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openDrawer() {
        _isDrawerOpen.value = true
    }

    fun closeDrawer() {
        _isDrawerOpen.value = false
        _searchQuery.value = ""
    }

    fun toggleDrawer() {
        if (_isDrawerOpen.value) closeDrawer() else openDrawer()
    }

    fun launch(app: AppInfo) {
        appsRepo.launchApp(app)
        closeDrawer()
    }

    fun updateSettings(transform: (LauncherSettings) -> LauncherSettings) {
        viewModelScope.launch {
            settingsRepo.update(transform)
            // Re-filter if hideSystem changed
            refreshApps()
        }
    }

    fun setHomeApps(apps: List<AppInfo>) {
        _homeApps.value = apps
    }

    fun setDockApps(apps: List<AppInfo>) {
        _dockApps.value = apps
    }
}

package apps.monkpad.kiddoguard.presenter.viewmodels

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.monkpad.kiddoguard.R
import apps.monkpad.kiddoguard.data.repository.MainRepository
import apps.monkpad.kiddoguard.models.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val packageManager: PackageManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _selectedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val selectedApps = _selectedApps.asStateFlow()

    private val _availableApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val availableApps = _availableApps.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            loadSelectedApps()
        }
    }

    fun loadAvailableApps() {
        viewModelScope.launch {
            _loading.value = true
            val apps = getInstalledApps(packageManager)
            _availableApps.value = apps
            _loading.value = false
        }
    }

    private fun getAppIcon(packageName: String): Drawable {
        return try {
            packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            ContextCompat.getDrawable(appContext, R.drawable.ic_launcher_foreground)!!
        }
    }

    private fun loadSelectedApps() {
        viewModelScope.launch {
            repository.getAllSelectedApp()
                .map { selectedAppEntities ->
                    selectedAppEntities.map { entity ->
                        AppInfo(
                            name = entity.name,
                            packageName = entity.packageName,
                            icon = getAppIcon(entity.packageName)
                        )
                    }
                }
                .collect { appInfos ->
                    _selectedApps.value = appInfos
                }
        }
    }

    suspend fun selectApp(selectedApp: AppInfo) {
        try {
            repository.insertSelectedApp(selectedApp)
            _selectedApps.value = _selectedApps.value + selectedApp
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun deselectApp(selectedApp: AppInfo) {
        try {
            repository.removeSelectedApp(selectedApp)
            _selectedApps.value = _selectedApps.value - selectedApp
        } catch (e: Exception) {
            // Handle error
        }
    }



    private suspend fun getInstalledApps(packageManager: PackageManager): List<AppInfo> {
        return withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }

            // Get all apps that can be launched (exclude non-launchable apps)
            val launchableApps = packageManager.queryIntentActivities(intent, 0)
            val apps = mutableListOf<AppInfo>()

            for (resolveInfo in launchableApps) {
                val packageName = resolveInfo.activityInfo.packageName
                val appName = packageManager.getApplicationLabel(resolveInfo.activityInfo.applicationInfo).toString()
                val appIcon = getAppIcon(packageName)

                // Filter to exclude non-user apps unless they are launchable
                val isSystemApp = resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                if (!isSystemApp || packageManager.getLaunchIntentForPackage(packageName) != null) {
                    apps.add(AppInfo(name = appName, icon = appIcon, packageName = packageName))
                }
            }
            apps
        }
    }



}

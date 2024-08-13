package apps.monkpad.kiddoguard.utils

import android.app.WallpaperManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import apps.monkpad.kiddoguard.models.AppInfo


fun getInstalledApps(packageManager: PackageManager): List<AppInfo> {
    val apps = mutableListOf<AppInfo>()
    val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    for (packageInfo in packages) {
        val appName = packageManager.getApplicationLabel(packageInfo).toString()
        val appIcon = packageManager.getApplicationIcon(packageInfo.packageName)
        val isSystemApp = packageInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0

        // Include both system apps and user-installed apps
        if (isSystemApp || packageInfo.flags and ApplicationInfo.FLAG_INSTALLED != 0) {
            apps.add(AppInfo(name = appName, icon = appIcon, packageName = packageInfo.packageName))
        }
    }
    return apps
}

fun getCurrentWallpaper(context: Context): ImageBitmap? {
    val wallpaperManager = WallpaperManager.getInstance(context)
    val wallpaperDrawable = wallpaperManager.drawable
    return if(wallpaperDrawable is BitmapDrawable){
        wallpaperDrawable.bitmap.asImageBitmap()
    } else {
        null
    }
}

fun drawableToImageBitmap(drawable: Drawable): ImageBitmap {
    return drawable.toBitmap().asImageBitmap()
}


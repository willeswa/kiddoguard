package apps.monkpad.kiddoguard.models

import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val icon: Drawable?,
    val packageName: String
)
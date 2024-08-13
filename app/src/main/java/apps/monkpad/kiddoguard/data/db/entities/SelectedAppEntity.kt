package apps.monkpad.kiddoguard.data.db.entities

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_apps")
data class SelectedAppEntity(
    @PrimaryKey
    val packageName: String,
    val name: String
)
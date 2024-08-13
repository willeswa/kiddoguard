package apps.monkpad.kiddoguard.data

import apps.monkpad.kiddoguard.data.db.entities.SelectedAppEntity
import apps.monkpad.kiddoguard.models.AppInfo

fun SelectedAppEntity.asSelectedAppModel() = AppInfo(
    packageName = packageName,
    name = name,
    icon = null
)

fun AppInfo.asSelectedAppEntity() = SelectedAppEntity(
    packageName = packageName,
    name = name
)
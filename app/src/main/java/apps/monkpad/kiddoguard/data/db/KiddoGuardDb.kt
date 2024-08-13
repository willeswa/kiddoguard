package apps.monkpad.kiddoguard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import apps.monkpad.kiddoguard.data.db.daos.SelectedAppDao
import apps.monkpad.kiddoguard.data.db.entities.SelectedAppEntity

@Database(entities = [SelectedAppEntity::class], version = 1, exportSchema = false)
abstract class KiddoGuardDb: RoomDatabase() {
    abstract  fun selectedAppDao(): SelectedAppDao
}
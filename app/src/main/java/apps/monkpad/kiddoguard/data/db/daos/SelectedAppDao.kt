package apps.monkpad.kiddoguard.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import apps.monkpad.kiddoguard.data.db.entities.SelectedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedAppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSelectedApp(selectedApp: SelectedAppEntity)

    @Query("SELECT * FROM selected_apps")
    fun getAllApps(): Flow<List<SelectedAppEntity>>

    @Delete
    suspend fun removeSelectedApp(selectedApp: SelectedAppEntity)
}
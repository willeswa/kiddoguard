package apps.monkpad.kiddoguard.data.repository

import apps.monkpad.kiddoguard.data.asSelectedAppEntity
import apps.monkpad.kiddoguard.data.asSelectedAppModel
import apps.monkpad.kiddoguard.data.db.daos.SelectedAppDao
import apps.monkpad.kiddoguard.models.AppInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepository @Inject constructor(private val selectedAppDao: SelectedAppDao){
    fun getAllSelectedApp(): Flow<List<AppInfo>> {
        return selectedAppDao.getAllApps().map {entities ->
            entities.map { it.asSelectedAppModel() }
        }
    }

    suspend fun insertSelectedApp(selectedApp: AppInfo){
        try {
            selectedAppDao.insertSelectedApp(selectedApp.asSelectedAppEntity())
        } catch (e: Exception){
            throw e
        }
    }

    suspend fun removeSelectedApp(selectedApp: AppInfo){
        try {
            selectedAppDao.removeSelectedApp(selectedApp.asSelectedAppEntity())
        } catch (e: Exception){
            throw e
        }
    }
}
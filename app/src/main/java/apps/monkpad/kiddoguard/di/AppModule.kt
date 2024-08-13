package apps.monkpad.kiddoguard.di

import android.content.Context
import android.content.pm.PackageManager
import androidx.room.Room
import apps.monkpad.kiddoguard.data.db.KiddoGuardDb
import apps.monkpad.kiddoguard.data.db.daos.SelectedAppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): KiddoGuardDb {
        return Room.databaseBuilder(
            context,
            KiddoGuardDb::class.java,
            "kiddo_guard_db",
        ).build()
    }

    @Provides
    fun providesDao(database: KiddoGuardDb): SelectedAppDao {
        return database.selectedAppDao()
    }

    @Provides
    fun providePackageManager(@ApplicationContext context: Context): PackageManager {
        return context.packageManager
    }
}

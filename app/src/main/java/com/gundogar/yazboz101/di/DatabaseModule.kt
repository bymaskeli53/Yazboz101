package com.gundogar.yazboz101.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gundogar.yazboz101.data.AppDatabase
import com.gundogar.yazboz101.data.YazbozDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * v1 -> v2: takım/bireysel oyun modunu saklamak için `gameMode` sütunu eklendi.
     * Mevcut tüm kayıtlar varsayılan olarak bireysel (INDIVIDUAL) kabul edilir.
     */
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE yazboz_item ADD COLUMN gameMode TEXT NOT NULL DEFAULT 'INDIVIDUAL'"
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideYazbozDao(db: AppDatabase): YazbozDao = db.yazbozDao()
}
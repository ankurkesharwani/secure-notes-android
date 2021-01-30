package com.ankur.securenotes.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DbHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DbConfig.DATABASE_NAME,
        null,
        DbConfig.DATABASE_VERSION
    ) {

    override fun onCreate(
        db: SQLiteDatabase
    ) {
        db.execSQL(MigrationHistoryEntity.CREATE)
        runMigrations(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        runMigrations(db)
    }

    private fun runMigrations(
        db: SQLiteDatabase
    ) {
        val migrationHistory: List<MigrationHistoryEntity> =
            MigrationHistoryDao().findAll(db)
                .sortedBy { it.version }
        var lastMigrationVersion: Int = 0
        if (migrationHistory.isNotEmpty()) {
            lastMigrationVersion = migrationHistory.last().version!!
        }

        val filtered = DbConfig.migrations.filter { it.matches(Regex("V[0-9]+_[A-Z]+.*")) }
        val sorted = filtered.sorted()
        val mapped = sorted.map {
            Pair(
                it.split("_")
                    .first()
                    .removePrefix(DbConfig.MIGRATION_PREFIX)
                    .toInt(),
                it
            )
        }
        for (i in mapped) {
            if (i.first <= lastMigrationVersion) {
                continue
            }

            if (i.first > DbConfig.DATABASE_VERSION) {
                break
            }
            val migrationClass: Class<AbstractMigration> =
                Class.forName(
                    "${DbConfig.MIGRATION_PACKAGE}.${i.second}") as Class<AbstractMigration>
            val migration: AbstractMigration = migrationClass.newInstance()
            Log.d("DbHelper", "Running migration: ${i.second}")
            migration.upgrade(db, i.first)
        }
    }
}
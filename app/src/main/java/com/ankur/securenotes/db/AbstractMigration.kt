package com.ankur.securenotes.db

import android.database.sqlite.SQLiteDatabase

abstract class AbstractMigration {
    abstract fun up(db: SQLiteDatabase)

    fun upgrade(db: SQLiteDatabase, version: Int) {
        up(db);

        val history = MigrationHistoryEntity()
        history.version = version
        MigrationHistoryDao().createNote(history, db)
    }
}
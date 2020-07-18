package com.ankur.securenotes.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ankur.securenotes.entities.*


class DbMigration(private val db: SQLiteDatabase, fromVersion: Int, toVersion: Int) {

    companion object {
        private const val TAG = "DbMigration"
    }

    fun onUpdate() {
        execSQL(NoteEntity.DROP)
        execSQL(PasswordEntity.DROP)
        execSQL(LabelEntity.DROP)

        DbCreate(db).create()
    }

    private fun execSQL(query: String) {
        Log.d(TAG, "Executing - $query")
        db.execSQL(query)
    }
}
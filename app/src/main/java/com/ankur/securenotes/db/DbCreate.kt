package com.ankur.securenotes.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ankur.securenotes.entities.*

class DbCreate(private val db: SQLiteDatabase) {

    companion object {
        private const val TAG = "DbCreate"
    }

    fun create() {
        execSQL(NoteEntity.CREATE)
        execSQL(PasswordEntity.CREATE)
        execSQL(LabelEntity.CREATE)
    }

    private fun execSQL(query: String) {
        Log.d(TAG, "Executing - $query")
        db.execSQL(query)
    }
}
package com.ankur.securenotes.db.migrations

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.db.AbstractMigration
import com.ankur.securenotes.entities.PasswordEntity

class V2_CreateTablePassword: AbstractMigration() {
    override fun up(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE password (
                "id" TEXT PRIMARY KEY,
	            "title"TEXT NOT NULL UNIQUE,
	            "username"NUMERIC NOT NULL,
	            "password" INTEGER NOT NULL DEFAULT 0,
                "archived" NUMERIC NOT NULL,
                "date_created" NUMERIC NOT NULL,
                "date_updated" NUMERIC NOT NULL
            );
        """)
    }
}
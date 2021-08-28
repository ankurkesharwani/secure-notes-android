package com.ankur.securenotes.db.migrations

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.db.AbstractMigration

class V2_CreateTablePassword : AbstractMigration() {
  override fun up(db: SQLiteDatabase) {
    db.execSQL(
      """
            CREATE TABLE password (
                "id" TEXT PRIMARY KEY,
                "title" TEXT NOT NULL UNIQUE,
                "url" TEXT,
                "email_id" TEXT,
                "phone_no" TEXT,
	              "username" NUMERIC NOT NULL,
	              "password" INTEGER NOT NULL DEFAULT 0,
                "archived" NUMERIC NOT NULL,
                "date_created" NUMERIC NOT NULL,
                "date_updated" NUMERIC NOT NULL
            );
        """
    )
  }
}
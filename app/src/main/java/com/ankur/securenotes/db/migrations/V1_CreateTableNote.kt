package com.ankur.securenotes.db.migrations

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.db.AbstractMigration

class V1_CreateTableNote : AbstractMigration() {
  override fun up(db: SQLiteDatabase) {
    db.execSQL(
      """
            CREATE TABLE note (
                "id" TEXT PRIMARY KEY,
	            "title" TEXT NOT NULL UNIQUE,
	            "body" NUMERIC NOT NULL,
	            "archived" INTEGER NOT NULL DEFAULT 0,
                "date_created" NUMERIC NOT NULL,
                "date_updated" NUMERIC NOT NULL
            );
        """
    )
  }
}
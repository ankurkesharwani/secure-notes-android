package com.ankur.securenotes.db.migration

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.db.AbstractMigration

class V3_CreateTableLabel : AbstractMigration() {
  override fun up(db: SQLiteDatabase) {
    db.execSQL(
      """
            CREATE TABLE label (
                "id" TEXT PRIMARY KEY,
                "title"TEXT NOT NULL UNIQUE
            );
        """
    )
  }
}
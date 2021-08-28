package com.ankur.securenotes.db

import android.database.Cursor
import java.util.*

class MigrationHistoryEntity(
  var id: Int? = null,
  var uuid: String? = null,
  var version: Int? = null,
  var createdAt: Date? = null,
  var updatedAt: Date? = null
) {
  fun updateFrom(cursor: Cursor) {
    id = DbUtil.getValue(cursor, COLUMN_ID, id)
    uuid = DbUtil.getValue(cursor, COLUMN_UUID, uuid)
    version = DbUtil.getValue(cursor, COLUMN_VERSION, version)
    createdAt = DbUtil.getValue(cursor, COLUMN_DATE_CREATED, createdAt)
    updatedAt = DbUtil.getValue(cursor, COLUMN_DATE_UPDATED, updatedAt)
  }

  companion object {
    const val TABLE_NAME = "migration_history"

    const val COLUMN_ID = "id"
    const val COLUMN_UUID = "uuid"
    const val COLUMN_VERSION = "version"
    const val COLUMN_DATE_CREATED = "created_at"
    const val COLUMN_DATE_UPDATED = "updated_at"

    const val CREATE = """
            CREATE TABLE $TABLE_NAME (
                "$COLUMN_ID" INTEGER PRIMARY KEY AUTOINCREMENT,
                "$COLUMN_UUID" TEXT NOT NULL UNIQUE,
	            "$COLUMN_VERSION" INTEGER NOT NULL UNIQUE,
                "$COLUMN_DATE_CREATED" NUMERIC NOT NULL,
                "$COLUMN_DATE_UPDATED" NUMERIC NOT NULL
            );
        """

    const val DROP = """
            DROP TABLE IF EXISTS $TABLE_NAME
        """
  }
}
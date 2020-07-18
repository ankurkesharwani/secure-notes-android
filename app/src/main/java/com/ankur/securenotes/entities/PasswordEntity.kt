package com.ankur.securenotes.entities

import android.database.Cursor
import com.ankur.securenotes.db.DbUtil
import java.util.*

data class PasswordEntity(var id: Int?,
                          var uuid: String?,
                          var title: String?,
                          var username: String?,
                          var password: String?,
                          var archived: Boolean?,
                          var createdAt: Date?,
                          var updatedAt: Date?) {

    fun updateFrom(cursor: Cursor) {
        id = DbUtil.getValue(cursor, COLUMN_ID, id)
        uuid = DbUtil.getValue(cursor, COLUMN_UUID, uuid)
        title = DbUtil.getValue(cursor, COLUMN_TITLE, title)
        username = DbUtil.getValue(cursor, COLUMN_USERNAME, username)
        password = DbUtil.getValue(cursor, COLUMN_PASSWORD, password)
        archived = DbUtil.getValue(cursor, COLUMN_ARCHIVED, archived)
        createdAt = DbUtil.getValue(cursor, COLUMN_DATE_CREATED, createdAt)
        updatedAt = DbUtil.getValue(cursor, COLUMN_DATE_UPDATED, updatedAt)
    }

    companion object {
        const val TABLE_NAME = "password"

        const val COLUMN_ID = "id"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_TITLE = "title"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ARCHIVED = "archived"
        const val COLUMN_DATE_CREATED = "date_created"
        const val COLUMN_DATE_UPDATED = "date_updated"


        const val CREATE = """
            CREATE TABLE $TABLE_NAME (
	            "$COLUMN_ID" INTEGER PRIMARY KEY AUTOINCREMENT,
                "$COLUMN_UUID" TEXT NOT NULL UNIQUE,
	            "$COLUMN_TITLE "TEXT NOT NULL UNIQUE,
	            "$COLUMN_USERNAME "NUMERIC NOT NULL,
	            "$COLUMN_PASSWORD" INTEGER NOT NULL DEFAULT 0,
                "$COLUMN_ARCHIVED" NUMERIC NOT NULL,
                "$COLUMN_DATE_CREATED" NUMERIC NOT NULL,
                "$COLUMN_DATE_UPDATED" NUMERIC NOT NULL
            );
        """

        const val DROP = """
            DROP TABLE IF EXISTS $TABLE_NAME
        """
    }
}
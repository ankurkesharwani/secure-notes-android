package com.ankur.securenotes.entities

import android.database.Cursor
import com.ankur.securenotes.db.DbUtil
import java.util.*

data class NoteEntity(var id: String? = null,
                      var title: String? = null,
                      var body: String? = null,
                      var archived: Boolean? = null,
                      var createdAt: Date? = null,
                      var updatedAt: Date? =  null) {

    fun updateFrom(cursor: Cursor) {
        id = DbUtil.getValue(cursor, COLUMN_ID, id)
        title = DbUtil.getValue(cursor, COLUMN_TITLE, title)
        body = DbUtil.getValue(cursor, COLUMN_BODY, body)
        archived = DbUtil.getValue(cursor, COLUMN_ARCHIVED, archived)
        createdAt = DbUtil.getValue(cursor, COLUMN_DATE_CREATED, createdAt)
        updatedAt = DbUtil.getValue(cursor, COLUMN_DATE_UPDATED, updatedAt)
    }

    companion object {
        const val TABLE_NAME = "note"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_BODY = "body"
        const val COLUMN_ARCHIVED = "archived"
        const val COLUMN_DATE_CREATED = "date_created"
        const val COLUMN_DATE_UPDATED = "date_updated"
    }
}
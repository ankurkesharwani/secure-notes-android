package com.ankur.securenotes.entities

import android.database.Cursor
import com.ankur.securenotes.db.DbUtil
import java.util.*

data class LabelEntity(var id: Int?,
                       var title: String?) {

    fun updateFrom(cursor: Cursor) {
        id = DbUtil.getValue(cursor, COLUMN_ID, id)
        title = DbUtil.getValue(cursor, COLUMN_TITLE, title)
    }

    companion object {
        const val TABLE_NAME = "label"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"

        const val CREATE = """
            CREATE TABLE $TABLE_NAME (
                "$COLUMN_ID" TEXT PRIMARY KEY,
	            "$COLUMN_TITLE "TEXT NOT NULL UNIQUE
            );
        """

        const val DROP = """
            DROP TABLE IF EXISTS $TABLE_NAME
        """
    }
}
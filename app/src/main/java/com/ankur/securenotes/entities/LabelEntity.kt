package com.ankur.securenotes.entities

import java.util.*

data class LabelEntity(var id: Int?,
                       var uuid: String?,
                       var title: String?) {
    companion object {
        const val TABLE_NAME = "label"

        const val COLUMN_ID = "id"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_TITLE = "title"

        const val CREATE = """
            CREATE TABLE $TABLE_NAME (
	            "$COLUMN_ID" INTEGER PRIMARY KEY AUTOINCREMENT,
                "$COLUMN_UUID" TEXT NOT NULL UNIQUE,
	            "$COLUMN_TITLE "TEXT NOT NULL UNIQUE
            );
        """

        const val DROP = """
            DROP TABLE IF EXISTS ${TABLE_NAME}
        """
    }
}
package com.ankur.securenotes.entity

import android.database.Cursor
import com.ankur.securenotes.db.DbUtil

data class LabelEntity(
  var id: Int?, var title: String?
) {

  fun updateFrom(cursor: Cursor) {
    id = DbUtil.getValue(cursor, COLUMN_ID, id)
    title = DbUtil.getValue(cursor, COLUMN_TITLE, title)
  }

  companion object {
    const val TABLE_NAME = "label"
    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
  }
}
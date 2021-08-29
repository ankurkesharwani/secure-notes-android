package com.ankur.securenotes.entity

import android.database.Cursor
import com.ankur.securenotes.db.DbUtil
import java.util.*

data class PasswordEntity(
  var id: String? = null,
  var title: String? = null,
  var url: String? = null,
  var email: String? = null,
  var phone: String? = null,
  var username: String? = null,
  var password: String? = null,
  var archived: Boolean? = null,
  var createdAt: Date? = null,
  var updatedAt: Date? = null
) {

  fun updateFrom(cursor: Cursor) {
    id = DbUtil.getValue(cursor, COLUMN_ID, id)
    title = DbUtil.getValue(cursor, COLUMN_TITLE, title)
    url = DbUtil.getValue(cursor, COLUMN_URL, url)
    email = DbUtil.getValue(cursor, COLUMN_EMAIL_ID, email)
    phone = DbUtil.getValue(cursor, COLUMN_PHONE_NO, phone)
    username = DbUtil.getValue(cursor, COLUMN_USERNAME, username)
    password = DbUtil.getValue(cursor, COLUMN_PASSWORD, password)
    archived = DbUtil.getValue(cursor, COLUMN_ARCHIVED, archived)
    createdAt = DbUtil.getValue(cursor, COLUMN_DATE_CREATED, createdAt)
    updatedAt = DbUtil.getValue(cursor, COLUMN_DATE_UPDATED, updatedAt)
  }

  companion object {
    const val TABLE_NAME = "password"
    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_URL = "url"
    const val COLUMN_EMAIL_ID = "email_id"
    const val COLUMN_PHONE_NO = "phone_no"
    const val COLUMN_USERNAME = "username"
    const val COLUMN_PASSWORD = "password"
    const val COLUMN_ARCHIVED = "archived"
    const val COLUMN_DATE_CREATED = "date_created"
    const val COLUMN_DATE_UPDATED = "date_updated"
  }
}
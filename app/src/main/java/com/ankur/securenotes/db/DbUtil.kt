package com.ankur.securenotes.db

import android.database.Cursor
import java.util.*

object DbUtil {
    inline fun <reified T> getValue(cursor: Cursor, name: String, default: T? = null): T? {
        val columnIndex = cursor.getColumnIndex(name)
        if (columnIndex == -1) {
            return default
        }

        when (T::class) {
            Int::class -> {
                return cursor.getInt(columnIndex) as T?
            }
            Double::class -> {
                return cursor.getDouble(columnIndex) as T?
            }
            String::class -> {
                return cursor.getString(columnIndex) as T?
            }
            Date::class -> {
                val epoch = cursor.getLong(columnIndex)
                return Date(epoch) as T?
            }
            Boolean::class -> {
                return (cursor.getInt(columnIndex) >= 0) as T?
            }
        }

        return null
    }
}
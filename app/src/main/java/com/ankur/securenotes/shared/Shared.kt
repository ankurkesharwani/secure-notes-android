package com.ankur.securenotes.shared

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.db.DbHelper
import com.ankur.securenotes.taskexecuter.SerialTaskExecutor

object Shared {

  var serialTaskExecutor: SerialTaskExecutor? = null
    get() {
      if (field == null) {
        field = SerialTaskExecutor()
      }

      return field!!
    }

  var store: MemStore? = null
    get() {
      if (field == null) {
        field = MemStore()
      }

      return field!!
    }

  @JvmStatic
  fun getWritableDatabase(context: Context): SQLiteDatabase {
    return DbHelper(context).writableDatabase
  }

  @JvmStatic
  fun getReadableDatabase(context: Context): SQLiteDatabase {
    return DbHelper(context).readableDatabase
  }
}
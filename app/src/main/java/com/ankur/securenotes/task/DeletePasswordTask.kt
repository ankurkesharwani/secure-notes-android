package com.ankur.securenotes.task

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.ankur.securenotes.dao.PasswordDao
import com.ankur.securenotes.entity.PasswordEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class DeletePasswordTask(var password: PasswordEntity, var db: SQLiteDatabase) : Task() {

  data class Result(var error: TaskError? = null)

  var result: Result? = null

  override fun exec() {
    if (password.id == null) {
      return
    }

    result = try {
      PasswordDao.deleteById(password.id!!, db)
      Result()
    } catch (exception: SQLiteException) {
      Result(TaskError(-1, exception.localizedMessage))
    }
  }
}
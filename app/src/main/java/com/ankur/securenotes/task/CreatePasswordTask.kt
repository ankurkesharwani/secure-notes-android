package com.ankur.securenotes.task

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.ankur.securenotes.dao.PasswordDao
import com.ankur.securenotes.entity.PasswordEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class CreatePasswordTask(var password: PasswordEntity, var db: SQLiteDatabase) : Task() {

  data class Result(var password: PasswordEntity?, var error: TaskError? = null)

  var result: Result? = null

  override fun exec() {
    result = try {
      val password = PasswordDao.createPassword(password, db)
      Result(password)
    } catch (exception: SQLiteException) {
      Result(null, TaskError(-1, exception.localizedMessage))
    }
  }
}
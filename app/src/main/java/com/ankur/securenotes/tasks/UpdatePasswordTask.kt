package com.ankur.securenotes.tasks

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.ankur.securenotes.daos.PasswordDao
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class UpdatePasswordTask(
    var password: PasswordEntity,
    var db: SQLiteDatabase
) : Task() {

    data class Result(
        var password: PasswordEntity?,
        var error: TaskError? = null
    )

    var result: Result? = null

    override fun exec() {
        result = try {
            val password = PasswordDao.updatePassword(password, db)
            Result(password)
        } catch (exception: SQLiteException) {
            Result(null, TaskError(-1, exception.localizedMessage))
        }
    }
}
package com.ankur.securenotes.tasks

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.daos.PasswordDao
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class GetAllPasswordsTask(
    var db: SQLiteDatabase
) : Task() {
    data class Result(
        var passwords: List<PasswordEntity>?,
        var error: TaskError? = null
    )

    var result: Result? = null

    override fun exec() {
        result = try {
            val passwords = PasswordDao.findAll(db)

            Result(passwords)
        } catch (exc: SQLiteConstraintException) {
            Result(null, TaskError(-1, exc.localizedMessage))
        } catch (exc: Exception) {
            Result(null, TaskError(-1, exc.localizedMessage))
        }
    }
}

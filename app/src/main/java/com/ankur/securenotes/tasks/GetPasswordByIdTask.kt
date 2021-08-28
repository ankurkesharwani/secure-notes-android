package com.ankur.securenotes.tasks

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.daos.PasswordDao
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class GetPasswordByIdTask(
    var id: String, var db: SQLiteDatabase
) : Task() {

    data class Result(
        var password: PasswordEntity?, var error: TaskError? = null
    )

    var result: Result? = null

    override fun exec() {
        result = try {
            val password = PasswordDao.findOneById(id, db)

            Result(password)
        } catch (exc: Exception) {
            Result(null, TaskError(-1, exc.localizedMessage))
        }
    }
}

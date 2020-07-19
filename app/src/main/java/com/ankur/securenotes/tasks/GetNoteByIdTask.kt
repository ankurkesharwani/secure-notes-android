package com.ankur.securenotes.tasks

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.daos.NotesDAO
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError
import java.lang.Exception

class GetNoteByIdTask(var UUID: String,
                      var db: SQLiteDatabase): Task() {
    data class Result(var note: NoteEntity?, var error: TaskError? = null)

    var result: Result? = null

    override fun exec() {
        result = try {
            val note = NotesDAO.findOneByUUID(UUID, db)

            Result(note)
        } catch (exc: Exception) {
            Result(null, TaskError(-1, exc.localizedMessage))
        }
    }
}

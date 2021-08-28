package com.ankur.securenotes.tasks

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.daos.NotesDao
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class GetNoteByIdTask(
  var id: String, var db: SQLiteDatabase
) : Task() {

  data class Result(
    var note: NoteEntity?, var error: TaskError? = null
  )

  var result: Result? = null

  override fun exec() {
    result = try {
      val note = NotesDao.findOneById(id, db)

      Result(note)
    } catch (exc: Exception) {
      Result(null, TaskError(-1, exc.localizedMessage))
    }
  }
}

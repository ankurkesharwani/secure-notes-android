package com.ankur.securenotes.task

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.dao.NotesDao
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class GetNoteByIdTask(var id: String, var db: SQLiteDatabase) : Task() {

  data class Result(var note: NoteEntity?, var error: TaskError? = null)

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

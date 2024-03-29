package com.ankur.securenotes.task

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.ankur.securenotes.dao.NotesDao
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class DeleteNoteTask(var note: NoteEntity, var db: SQLiteDatabase) : Task() {

  data class Result(var error: TaskError? = null)

  var result: Result? = null

  override fun exec() {
    if (note.id == null) {
      return
    }

    result = try {
      NotesDao.deleteById(note.id!!, db)
      Result()
    } catch (exception: SQLiteException) {
      Result(TaskError(-1, exception.localizedMessage))
    }
  }
}
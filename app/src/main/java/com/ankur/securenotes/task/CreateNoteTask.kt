package com.ankur.securenotes.task

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.ankur.securenotes.dao.NotesDao
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class CreateNoteTask(var note: NoteEntity, var db: SQLiteDatabase) : Task() {

  data class Result(var note: NoteEntity?, var error: TaskError? = null)

  var result: Result? = null

  override fun exec() {
    result = try {
      val note = NotesDao.createNote(note, db)
      Result(note)
    } catch (exception: SQLiteException) {
      Result(null, TaskError(-1, exception.localizedMessage))
    }
  }
}
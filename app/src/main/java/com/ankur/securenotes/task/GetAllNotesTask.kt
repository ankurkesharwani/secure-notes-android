package com.ankur.securenotes.task

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.dao.NotesDao
import com.ankur.securenotes.entity.NoteEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class GetAllNotesTask(var db: SQLiteDatabase) : Task() {

  data class Result(var notes: List<NoteEntity>?, var error: TaskError? = null)

  var result: Result? = null

  override fun exec() {
    result = try {
      val notes = NotesDao.findAll(db)

      Result(notes)
    } catch (exc: SQLiteConstraintException) {
      Result(null, TaskError(-1, exc.localizedMessage))
    } catch (exc: Exception) {
      Result(null, TaskError(-1, exc.localizedMessage))
    }
  }
}

package com.ankur.securenotes.tasks

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.ankur.securenotes.daos.NotesDAO
import com.ankur.securenotes.entities.NoteEntity
import com.ankur.securenotes.taskexecuter.Task
import com.ankur.securenotes.taskexecuter.TaskError

class CreateNoteTask(var note: NoteEntity, var db: SQLiteDatabase): Task() {
    data class Result(var note: NoteEntity?, var error: TaskError? = null)

    var result: Result? = null

    override fun exec() {
        result = try {
            val note = NotesDAO.createNote(note, db)
            Result(note)
        } catch (exception: SQLiteException) {
            Result(null, TaskError(-1, exception.localizedMessage))
        }
    }
}
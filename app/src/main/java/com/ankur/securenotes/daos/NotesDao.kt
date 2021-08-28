package com.ankur.securenotes.daos

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.entities.NoteEntity
import java.util.*

object NotesDao {
    fun findOneById(id: String, db: SQLiteDatabase): NoteEntity? {
        val query = """
            SELECT * FROM "${NoteEntity.TABLE_NAME}"
            WHERE "${NoteEntity.COLUMN_ID}" = "$id"
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToNext()) {
            val note = NoteEntity()
            note.updateFrom(cursor)

            return note
        }

        return null
    }

    fun findAll(db: SQLiteDatabase): List<NoteEntity> {
        val query = """
            SELECT * FROM "${NoteEntity.TABLE_NAME}"
        """.trimIndent()

        val notes = arrayListOf<NoteEntity>()
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            var note = NoteEntity()
            note.updateFrom(cursor)
            notes.add(note)
        }

        return notes
    }

    fun createNote(note: NoteEntity, db: SQLiteDatabase): NoteEntity? {
        val id = UUID.randomUUID().toString()
        val current = Date()
        val query = """
            INSERT INTO "${NoteEntity.TABLE_NAME}"
            (
                "${NoteEntity.COLUMN_ID}",
                "${NoteEntity.COLUMN_TITLE}",
                "${NoteEntity.COLUMN_BODY}",
                "${NoteEntity.COLUMN_ARCHIVED}",
                "${NoteEntity.COLUMN_DATE_CREATED}",
                "${NoteEntity.COLUMN_DATE_UPDATED}"
            ) 
            VALUES 
            (
                "$id",
                "${note.title}",
                "${note.body}",
                "${note.archived}",
                "${current.time}",
                "${current.time}"
            )
        """.trimIndent()

        db.execSQL(query)

        return findOneById(id, db)
    }

    fun updateNote(note: NoteEntity, db: SQLiteDatabase): NoteEntity? {
        if (note.id == null) {
            return null
        }

        val current = Date()
        val query = """
            UPDATE "${NoteEntity.TABLE_NAME}" 
            SET
                "${NoteEntity.COLUMN_TITLE}" = "${note.title}",
                "${NoteEntity.COLUMN_BODY}" = "${note.body}",
                "${NoteEntity.COLUMN_ARCHIVED}" = "${note.archived}",
                "${NoteEntity.COLUMN_DATE_UPDATED}" = "${current.time}"
            WHERE
                "${NoteEntity.COLUMN_ID}" = "${note.id}"
        """.trimIndent()

        db.execSQL(query)

        return findOneById(note.id!!, db)
    }

    fun deleteById(id: String, db: SQLiteDatabase) {
        val query = """
            DELETE FROM "${NoteEntity.TABLE_NAME}"
            WHERE "${NoteEntity.COLUMN_ID}" = "$id"
        """.trimIndent()

        db.execSQL(query)
    }
}
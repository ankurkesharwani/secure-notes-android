package com.ankur.securenotes.db

import android.database.sqlite.SQLiteDatabase
import java.util.*

class MigrationHistoryDao {
    fun findOneById(id: Int, db: SQLiteDatabase): MigrationHistoryEntity? {
        val query = """
            SELECT * FROM "${MigrationHistoryEntity.TABLE_NAME}"
            WHERE "${MigrationHistoryEntity.COLUMN_ID}" = "$id"
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToNext()) {
            val history = MigrationHistoryEntity()
            history.updateFrom(cursor)

            return history
        }

        return null
    }

    fun findOneByUUID(uuid: String, db: SQLiteDatabase): MigrationHistoryEntity? {
        val query = """
            SELECT * FROM "${MigrationHistoryEntity.TABLE_NAME}"
            WHERE "${MigrationHistoryEntity.COLUMN_UUID}" = "$uuid"
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToNext()) {
            val history = MigrationHistoryEntity()
            history.updateFrom(cursor)

            return history
        }

        return null
    }

    fun findAll(db: SQLiteDatabase): List<MigrationHistoryEntity> {
        val query = """
            SELECT * FROM "${MigrationHistoryEntity.TABLE_NAME}"
        """.trimIndent()

        val notes = arrayListOf<MigrationHistoryEntity>()
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            var history = MigrationHistoryEntity()
            history.updateFrom(cursor)
            notes.add(history)
        }

        return notes
    }

    fun createNote(history: MigrationHistoryEntity, db: SQLiteDatabase): MigrationHistoryEntity? {
        val current = Date()
        val uuid = UUID.randomUUID().toString()
        val query = """
            INSERT INTO "${MigrationHistoryEntity.TABLE_NAME}"
            (
                "${MigrationHistoryEntity.COLUMN_UUID}",
                "${MigrationHistoryEntity.COLUMN_VERSION}",
                "${MigrationHistoryEntity.COLUMN_DATE_CREATED}",
                "${MigrationHistoryEntity.COLUMN_DATE_UPDATED}"
            ) 
            VALUES 
            (
                "$uuid",
                "${history.version}",
                "${current.time}",
                "${current.time}"
            )
        """.trimIndent()

        db.execSQL(query)

        return findOneByUUID(uuid, db)
    }

    fun updateNote(history: MigrationHistoryEntity, db: SQLiteDatabase): MigrationHistoryEntity? {
        if (history.id == null) {
            return null
        }

        val current = Date()
        val query = """
            UPDATE "${MigrationHistoryEntity.TABLE_NAME}" 
            SET
                "${MigrationHistoryEntity.COLUMN_VERSION}" = "${history.version}",
                "${MigrationHistoryEntity.COLUMN_DATE_UPDATED}" = "${current.time}"
            WHERE
                "${MigrationHistoryEntity.COLUMN_ID}" = "${history.id}"
        """.trimIndent()

        db.execSQL(query)

        return findOneById(history.id!!, db)
    }

    fun deleteById(id: Int, db: SQLiteDatabase) {
        val query = """
            DELETE FROM "${MigrationHistoryEntity.TABLE_NAME}"
            WHERE "${MigrationHistoryEntity.COLUMN_ID}" = "$id"
        """.trimIndent()

        db.execSQL(query)
    }

    fun deleteByUUID(uuid: String, db: SQLiteDatabase) {
        val query = """
            DELETE FROM "${MigrationHistoryEntity.TABLE_NAME}"
            WHERE "${MigrationHistoryEntity.COLUMN_UUID}" = "$uuid"
        """.trimIndent()

        db.execSQL(query)
    }

    fun deleteWhereVersionIs(version: Int, db: SQLiteDatabase) {
        val query = """
            DELETE FROM "${MigrationHistoryEntity.TABLE_NAME}"
            WHERE "${MigrationHistoryEntity.COLUMN_VERSION}" = "$version"
        """.trimIndent()

        db.execSQL(query)
    }
}

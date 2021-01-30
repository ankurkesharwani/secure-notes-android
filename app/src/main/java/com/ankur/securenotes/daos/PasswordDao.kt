package com.ankur.securenotes.daos

import android.database.sqlite.SQLiteDatabase
import com.ankur.securenotes.entities.PasswordEntity
import java.util.*

object PasswordDao {
    fun findOneById(id: String, db: SQLiteDatabase): PasswordEntity? {
        val query = """
            SELECT * FROM "${PasswordEntity.TABLE_NAME}"
            WHERE "${PasswordEntity.COLUMN_ID}" = "$id"
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToNext()) {
            val password = PasswordEntity()
            password.updateFrom(cursor)

            return password
        }

        return null
    }

    fun findAll(db: SQLiteDatabase): List<PasswordEntity> {
        val query = """
            SELECT * FROM "${PasswordEntity.TABLE_NAME}"
        """.trimIndent()

        val passwords = arrayListOf<PasswordEntity>()
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            var password = PasswordEntity()
            password.updateFrom(cursor)
            passwords.add(password)
        }

        return passwords
    }

    fun createPassword(
        password: PasswordEntity,
        db: SQLiteDatabase
    ): PasswordEntity? {
        val id = UUID.randomUUID().toString()
        val current = Date()
        val query = """
            INSERT INTO "${PasswordEntity.TABLE_NAME}"
            (
                "${PasswordEntity.COLUMN_ID}",
                "${PasswordEntity.COLUMN_TITLE}",
                "${PasswordEntity.COLUMN_URL}",
                "${PasswordEntity.COLUMN_EMAIL_ID}",
                "${PasswordEntity.COLUMN_PHONE_NO}",
                "${PasswordEntity.COLUMN_USERNAME}",                
                "${PasswordEntity.COLUMN_PASSWORD}",
                "${PasswordEntity.COLUMN_ARCHIVED}",
                "${PasswordEntity.COLUMN_DATE_CREATED}",
                "${PasswordEntity.COLUMN_DATE_UPDATED}"
            ) 
            VALUES 
            (
                "$id",
                "${password.title}",
                "${password.url}",
                "${password.email}",
                "${password.phone}",
                "${password.username}",
                "${password.password}",
                "${password.archived}",
                "${current.time}",
                "${current.time}"
            )
        """.trimIndent()

        db.execSQL(query)

        return findOneById(id, db)
    }

    fun updatePassword(
        password: PasswordEntity,
        db: SQLiteDatabase
    ): PasswordEntity? {
        if (password.id == null) {
            return null
        }

        val current = Date()
        val query = """
            UPDATE "${PasswordEntity.TABLE_NAME}" 
            SET
                "${PasswordEntity.COLUMN_TITLE}" = "${password.title}",
                "${PasswordEntity.COLUMN_URL}" = "${password.url}",
                "${PasswordEntity.COLUMN_EMAIL_ID}" = "${password.email}",
                "${PasswordEntity.COLUMN_PHONE_NO}" = "${password.phone}",
                "${PasswordEntity.COLUMN_USERNAME}" = "${password.username}",
                "${PasswordEntity.COLUMN_PASSWORD}" = "${password.password}",
                "${PasswordEntity.COLUMN_ARCHIVED}" = "${password.archived}",
                "${PasswordEntity.COLUMN_DATE_UPDATED}" = "${current.time}"
            WHERE
                "${PasswordEntity.COLUMN_ID}" = "${password.id}"
        """.trimIndent()

        db.execSQL(query)

        return findOneById(password.id!!, db)
    }

    fun deleteById(
        id: String,
        db: SQLiteDatabase
    ) {
        val query = """
            DELETE FROM "${PasswordEntity.TABLE_NAME}"
            WHERE "${PasswordEntity.COLUMN_ID}" = "$id"
        """.trimIndent()

        db.execSQL(query)
    }
}
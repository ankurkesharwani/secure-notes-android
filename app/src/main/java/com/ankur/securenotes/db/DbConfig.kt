package com.ankur.securenotes.db

object DbConfig {

    // Database Version
    const val DATABASE_VERSION = 3

    // Database Name
    const val DATABASE_NAME = "securenotes_db"

    const val MIGRATION_PREFIX = "V"
    const val MIGRATION_PACKAGE = "com.ankur.securenotes.db.migrations"

    val migrations = arrayOf(
        "V1_CreateTableNote",
        "V2_CreateTablePassword",
        "V3_CreateTableLabel"
    )
}
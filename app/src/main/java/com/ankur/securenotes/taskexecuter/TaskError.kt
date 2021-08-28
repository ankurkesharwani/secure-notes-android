package com.ankur.securenotes.taskexecuter

data class TaskError(
    val code: Int, val message: String? = null
)
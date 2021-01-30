package com.ankur.securenotes.taskexecuter

import android.os.Handler
import android.os.HandlerThread
import java.lang.ref.WeakReference

/**
 * A worker that executes task serially as they arrive.
 */
class SerialTaskExecutor : Task.ExecutorListener {
    interface Listener {
        fun onTaskStarted(task: Task)
        fun onTaskFinished(task: Task)
    }

    // Other definitions

    private companion object {
        fun newHandlerThreadName(): String {
            return "[SerialTaskWorker: HandlerThread - ${latestHandlerThreadId++}]"
        }

        var latestHandlerThreadId: Int = 0
    }

    // Private properties

    private var handlerThread: HandlerThread? = null
    private var workerHandler: Handler? = null
    private val mainHandler: Handler
    private var latestStartId: Long

    private var taskIdMap: MutableMap<String, Long> = mutableMapOf()
    private var tasksMap: MutableMap<String, Task> = mutableMapOf()
    private var notificationMap: MutableMap<String, WeakReference<Listener>> = mutableMapOf()

    init {
        workerHandler = null
        mainHandler = Handler()
        latestStartId = 0
    }

    // Public Methods

    /**
     * Executes a Task.
     *
     * @property task The task to execute.
     */
    fun exec(
        task: Task,
        listener: Listener
    ) {
        if (handlerThread == null) {
            handlerThread = HandlerThread(newHandlerThreadName())
            handlerThread?.start()
            workerHandler = Handler(handlerThread!!.looper)
        }

        taskIdMap[task.name] = ++latestStartId
        tasksMap[task.name] = task
        notificationMap[task.name] = WeakReference(listener)

        task.setParams(this, mainHandler)
        workerHandler?.post(task.runnable)
    }

    /**
     * Forcefully stops execution.
     */
    fun quit() {
        handlerThread?.quit()
        workerHandler = null
        handlerThread = null
        latestStartId = 0
        tasksMap.clear()
        notificationMap.clear()
        tasksMap.clear()
    }

    override fun onTaskStarted(name: String) {
        val task = tasksMap[name]!!
        val listener = notificationMap[name]

        if (listener != null) {
            listener.get()
                ?.onTaskStarted(task)
        }
    }

    override fun onTaskFinished(name: String) {
        val task = tasksMap.remove(name)
        val listener = notificationMap.remove(name)
        val startId = taskIdMap.remove(name)

        if (task != null) {
            if (listener != null) {
                listener.get()
                    ?.onTaskFinished(task)
            }

            if (startId == latestStartId) {
                handlerThread?.quit()
                workerHandler = null
                handlerThread = null
                latestStartId = 0
            }
        }
    }
}
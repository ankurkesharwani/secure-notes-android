package com.ankur.securenotes.taskexecuter

import android.os.Handler
import java.lang.ref.WeakReference
import java.util.*

abstract class Task() {
    interface ExecutorListener {
        fun onTaskStarted(name: String)
        fun onTaskFinished(name: String)
    }

    private var callbackHandler: WeakReference<Handler>? = null
    private var callback: WeakReference<ExecutorListener>? = null


    val name: String = UUID.randomUUID()
        .toString()

    var runnable = Runnable {
        callbackHandler?.get()
            ?.post {
                callback?.get()
                    ?.onTaskStarted(name)
            }

        exec()

        callbackHandler?.get()
            ?.post {
                callback?.get()
                    ?.onTaskFinished(name)
            }
    }

    internal fun setParams(
        callBack: ExecutorListener,
        handler: Handler
    ) {
        this.callbackHandler = WeakReference(handler)
        this.callback = WeakReference(callBack)
    }

    abstract fun exec()
}
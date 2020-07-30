package com.ankur.securenotes.ui.fragments.note_viewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ankur.securenotes.R

class NoteViewerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_viewer, container, false)
    }

    companion object {
        @JvmField
        val TAG = this::class.java.name

        const val PARAM_NOTE_ID = "noteId"
    }
}
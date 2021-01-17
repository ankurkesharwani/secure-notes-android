package com.ankur.securenotes.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.ui.fragments.password_editor.PasswordEditorFragment
import kotlinx.android.synthetic.main.activity_password_editor.*

class PasswordEditorActivity : AppCompatActivity() {

    private var mode: String? = MODE_CREATE
    private var menuItemIdsValidForMode: Array<Int> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_editor)
        setSupportActionBar(findViewById(R.id.toolbar))

        setArgs()
        setupToolbar()
        showPasswordEditorFragment()
    }

    private fun setArgs() {
        mode = intent.extras?.get(NoteEditorActivity.PARAM_MODE_FLAG) as? String
    }

    private fun setupToolbar() {
        when(mode) {
            NoteEditorActivity.MODE_VIEW -> {
                supportActionBar?.title = getString(R.string.note_editor_title_view_note)
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miEditButton, R.id.miDeleteButton)
            }

            NoteEditorActivity.MODE_CREATE -> {
                supportActionBar?.title = getString(R.string.note_editor_title_new_note)
                toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
            }

            NoteEditorActivity.MODE_EDIT -> {
                supportActionBar?.title = getString(R.string.note_editor_title_edit_note)
                toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
            }
        }
    }

    private fun findFragment(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    private fun isFragmentPresent(tag: String): Boolean {
        return findFragment(tag) != null
    }

    private fun showPasswordEditorFragment() {
        if (isFragmentPresent(PasswordEditorFragment.TAG)) {
            return
        }

        // Add the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = PasswordEditorFragment()
        fragmentTransaction.add(
            R.id.fragmentContainer, fragment,
            PasswordEditorFragment.TAG
        )
        fragmentTransaction.commit()
    }

    private fun hidePasswordEditorFragment() {
        val fragment = findFragment(PasswordEditorFragment.TAG) ?: return

        // Remove the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    // region Overridden Methods
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_note_editor, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miSaveButton -> {
                // savePassword()

                true
            }

            R.id.miDeleteButton -> {
                // showDeletePasswordConfirmationDialog()

                true
            }

            R.id.miEditButton -> {
                // editPassword()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val iterator = menu?.iterator()
        val menuItemsToRemove: MutableList<Int> = mutableListOf()
        while (iterator?.hasNext() == true) {
            val menuItem = iterator.next()
            if (!menuItemIdsValidForMode.contains(menuItem.itemId)) {
                menuItemsToRemove.add(menuItem.itemId)
            }
        }

        menuItemsToRemove.forEach {
            menu?.removeItem(it)
        }

        return super.onPrepareOptionsMenu(menu)
    }
    // endregion

    companion object {
        const val PARAM_MODE_FLAG = "PARAM_MODE_FLAG"

        const val MODE_CREATE = "MODE_CREATE"
        const val MODE_EDIT = "MODE_EDIT"
        const val MODE_VIEW = "MODE_VIEW"
    }

}
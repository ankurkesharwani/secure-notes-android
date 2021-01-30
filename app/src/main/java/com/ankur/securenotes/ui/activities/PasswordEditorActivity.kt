package com.ankur.securenotes.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R
import com.ankur.securenotes.entities.PasswordEntity
import com.ankur.securenotes.ui.fragments.password.editor.PasswordEditorFragment
import com.ankur.securenotes.ui.fragments.note.editor.NoteEditorFragment
import kotlinx.android.synthetic.main.activity_password_editor.*
import java.lang.ref.WeakReference

class PasswordEditorActivity : AppCompatActivity(),
    PasswordEditorFragment.Listener {

    // region Properties
    private var mode: String? = MODE_CREATE
    private var passwordId: String? = null
    private var menuItemIdsValidForMode: Array<Int> = emptyArray()
    // endregion

    // region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_editor)
        setSupportActionBar(findViewById(R.id.toolbar))

        setArgs()
        setupToolbar()
        showPasswordEditorFragment()
        setupActionListeners()
    }
    // endregion

    // region Methods
    private fun setupToolbar() {
        when (mode) {
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

    private fun setArgs() {
        mode = intent.extras?.get(PARAM_MODE_FLAG) as? String
        passwordId = intent.extras?.get(PARAM_PASSWORD_ID) as? String
    }

    private fun setupActionListeners() {
        toolbar.setNavigationOnClickListener {
            when (mode) {
                MODE_VIEW -> {
                    finish()
                }

                MODE_CREATE -> {
                    showCancelCreateConfirmationDialog()
                }

                MODE_EDIT -> {
                    showDiscardChangesConfirmationDialog()
                }
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
        val passwordEditorFragment = PasswordEditorFragment()
        passwordEditorFragment.setListener(this)

        // Set arguments
        val bundle = getBundleForChildFragment()
        if (bundle.keySet().count() > 0) {
            passwordEditorFragment.arguments = bundle
        }

        // Show
        fragmentTransaction.add(
            R.id.fragmentContainer,
            passwordEditorFragment,
            NoteEditorFragment.TAG
        )
        fragmentTransaction.commit()
    }

    private fun getBundleForChildFragment(): Bundle {
        val bundle = Bundle()
        if (hasNoteToView()) {
            bundle.putString(PasswordEditorFragment.PARAM_PASSWORD_ID, passwordId)
        }
        if (isInViewMode()) {
            bundle.putString(
                PasswordEditorFragment.PARAM_MODE_FLAG,
                PasswordEditorFragment.MODE_VIEW
            )
        } else if (isInEditMode()) {
            bundle.putString(
                PasswordEditorFragment.PARAM_MODE_FLAG,
                PasswordEditorFragment.MODE_EDIT
            )
        }

        return bundle
    }

    private fun hasNoteToView(): Boolean {
        return ((mode == MODE_EDIT || mode == MODE_VIEW) && passwordId != null)
    }

    private fun isInViewMode(): Boolean {
        return (mode == MODE_VIEW)
    }

    private fun isInEditMode(): Boolean {
        return (mode == MODE_VIEW)
    }

    private fun hidePasswordEditorFragment() {
        val fragment = findFragment(PasswordEditorFragment.TAG) ?: return

        // Remove the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    private fun savePassword() {
        val passwordEditorFragment =
            findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        passwordEditorFragment?.savePassword()
    }

    private fun editPassword() {
        // Set Activity mode
        mode = MODE_EDIT

        // Set fragment mode
        val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        fragment?.setMode(PasswordEditorFragment.MODE_EDIT)

        // Refresh the toolbar
        setupToolbar()
        invalidateOptionsMenu()
    }

    private fun showDeletePasswordConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.password_editor_delete_confirmation_dialog_title))
            .setMessage(getString(R.string.password_editor_delete_confirmation_dialog_body))
            .setPositiveButton(android.R.string.yes) { _, _ ->
                deletePassword()
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun showDiscardChangesConfirmationDialog() {
        val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        if (fragment?.hasEditableChanges() == false) {
            discardChangesAndSwitchToViewMode()

            return
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.password_editor_discard_changes_dialog_title))
            .setMessage(getString(R.string.password_editor_discard_changes_dialog_body))
            .setPositiveButton(android.R.string.yes) { _, _ ->
                discardChangesAndSwitchToViewMode()
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun showCancelCreateConfirmationDialog() {
        val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        if (fragment?.hasEditableChanges() == false) {
            cancelPasswordCreate()

            return
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.password_editor_cancel_create_dialog_title))
            .setMessage(getString(R.string.password_editor_cancel_create_dialog_body))
            .setPositiveButton(android.R.string.yes) { _, _ ->
                cancelPasswordCreate()
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun discardChangesAndSwitchToViewMode() {
        // Set Activity mode
        mode = MODE_VIEW

        // Set fragment mode
        val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        fragment?.discardChanges()
        fragment?.setMode(PasswordEditorFragment.MODE_VIEW)

        // Refresh the toolbar
        setupToolbar()
        invalidateOptionsMenu()
    }

    private fun cancelPasswordCreate() {
        finish()
    }

    private fun deletePassword() {
        // Set fragment mode
        val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        fragment?.deletePassword()
    }

    // endregion

    // region Overridden Methods
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_note_editor, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miSaveButton -> {
                savePassword()

                true
            }

            R.id.miDeleteButton -> {
                showDeletePasswordConfirmationDialog()

                true
            }

            R.id.miEditButton -> {
                editPassword()

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

    // region PasswordEditorFragment.Listener
    override fun onPasswordSaved(
        password: PasswordEntity,
        fragment: WeakReference<Fragment>
    ) {
        Toast.makeText(this, getString(R.string.password_editor_message_note_saved), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onPasswordSavingFailed(
        password: PasswordEntity?,
        message: String?,
        fragment: WeakReference<Fragment>
    ) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPasswordDeleted(
        password: PasswordEntity,
        fragment: WeakReference<Fragment>
    ) {
        Toast.makeText(this, getString(R.string.password_editor_message_note_delete), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onPasswordDeletionFailed(
        password: PasswordEntity,
        message: String?,
        fragment: WeakReference<Fragment>
    ) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    // endregion

    companion object {
        const val PARAM_PASSWORD_ID = "PARAM_PASSWORD_ID"
        const val PARAM_MODE_FLAG = "PARAM_MODE_FLAG"

        const val MODE_CREATE = "MODE_CREATE"
        const val MODE_EDIT = "MODE_EDIT"
        const val MODE_VIEW = "MODE_VIEW"
    }
}
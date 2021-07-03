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
import com.ankur.securenotes.ui.fragments.password.viewer.PasswordViewerFragment
import kotlinx.android.synthetic.main.activity_password_editor.*
import java.lang.ref.WeakReference

class PasswordEditorActivity : AppCompatActivity(), PasswordEditorFragment.Listener {

    // region Properties

    private var shownFragmentTag: String? = null
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
        showStartingFragment()
        setupActionListeners()
    }

    // endregion


    // region Methods

    private fun setArgs() {
        mode = intent.extras?.get(PARAM_MODE_FLAG) as? String
        passwordId = intent.extras?.get(PARAM_PASSWORD_ID) as? String
    }

    private fun setupToolbar() {
        when (mode) {
            MODE_VIEW -> {
                supportActionBar?.title = getString(R.string.password_editor_title_view_password)
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miEditButton, R.id.miDeleteButton)
            }

            MODE_CREATE -> {
                supportActionBar?.title = getString(R.string.password_editor_title_new_password)
                toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
            }

            MODE_EDIT -> {
                supportActionBar?.title = getString(R.string.password_editor_title_edit_password)
                toolbar.setNavigationIcon(R.drawable.ic_cancel_primary_24dp)
                menuItemIdsValidForMode = arrayOf(R.id.miSaveButton)
            }
        }
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

    private fun showStartingFragment() {
        if (isInViewMode()) {
            showPasswordViewerFragment()
        } else if (isInEditOrCreateMode()) {
            showPasswordEditorFragment()
        }
    }

    private fun showPasswordViewerFragment() {
        showFragment(PasswordViewerFragment.TAG, getBundleForChildFragment())
    }

    private fun showPasswordEditorFragment() {
        var fragment = showFragment(PasswordEditorFragment.TAG, getBundleForChildFragment())
        (fragment as? PasswordEditorFragment)?.setListener(this)
    }

    private fun getBundleForChildFragment(): Bundle {
        val bundle = Bundle()
        if (hasPasswordToView()) {
            bundle.putString(PasswordEditorFragment.PARAM_PASSWORD_ID, passwordId)
        }

        return bundle
    }

    private fun hasPasswordToView(): Boolean {
        return ((mode == MODE_EDIT || mode == MODE_VIEW) && passwordId != null)
    }

    private fun isInViewMode(): Boolean {
        return (mode == MODE_VIEW)
    }

    private fun isInEditOrCreateMode(): Boolean {
        return (mode == MODE_EDIT || mode == MODE_CREATE)
    }

    private fun savePassword() {
        val passwordEditorFragment =
            findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        passwordEditorFragment?.savePassword()
    }

    private fun editPassword() { // Set Activity mode
        mode = MODE_EDIT

        showPasswordEditorFragment()

        // Refresh the toolbar
        setupToolbar()
        invalidateOptionsMenu()
    }

    private fun deletePassword() { // Set fragment mode
        val fragment = findFragment(PasswordEditorFragment.TAG) as? PasswordEditorFragment
        fragment?.deletePassword()
    }

    // region Dialog options

    private fun showDeletePasswordConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.password_editor_delete_confirmation_dialog_title))
            .setMessage(getString(R.string.password_editor_delete_confirmation_dialog_body))
            .setPositiveButton(android.R.string.yes) { _, _ ->
                deletePassword()
            }.setNegativeButton(android.R.string.no, null).show()
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
            }.setNegativeButton(android.R.string.no, null).show()
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
            }.setNegativeButton(android.R.string.no, null).show()
    }

    private fun discardChangesAndSwitchToViewMode() {
        mode = MODE_VIEW
        showPasswordViewerFragment()

        // Refresh the toolbar
        setupToolbar()
        invalidateOptionsMenu()
    }

    private fun cancelPasswordCreate() {
        finish()
    }

    // endregion


    // region Helper Methods

    private fun isFragmentPresent(tag: String): Boolean {
        return findFragment(tag) != null
    }

    private fun findFragment(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    private fun showFragment(tag: String, bundleArgs: Bundle?): Fragment? {
        if (isFragmentPresent(tag)) {
            return null
        }

        // Add the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = newFragment(tag) ?: return null

        // Set arguments
        if (bundleArgs != null) {
            if (bundleArgs.keySet().count() > 0) {
                fragment.arguments = bundleArgs
            }
        }

        // Show
        if (shownFragmentTag != null) {
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, tag)
        } else {
            fragmentTransaction.add(R.id.fragmentContainer, fragment, tag)
        }
        fragmentTransaction.commit()
        shownFragmentTag = tag
        return fragment
    }

    private fun removeFragment(tag: String): Fragment? {
        val fragment = findFragment(tag) ?: return null

        // Remove the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
        shownFragmentTag = tag

        return fragment
    }

    private fun newFragment(tag: String): Fragment? {
        when (tag) {
            PasswordViewerFragment.TAG -> return PasswordViewerFragment()
            PasswordEditorFragment.TAG -> return PasswordEditorFragment()
        }

        return null
    }

    // endregion

    // endregion


    // region Overridden Methods

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_password_editor, menu)

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

        menuItemsToRemove.forEach { menu?.removeItem(it) }

        return super.onPrepareOptionsMenu(menu)
    }

    // endregion


    // region PasswordEditorFragment.Listener

    override fun onPasswordSaved(
        password: PasswordEntity, fragment: WeakReference<Fragment>
    ) {
        Toast.makeText(this, getString(R.string.password_editor_message_password_saved),
            Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onPasswordSavingFailed(
        password: PasswordEntity?, message: String?, fragment: WeakReference<Fragment>
    ) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPasswordDeleted(
        password: PasswordEntity, fragment: WeakReference<Fragment>
    ) {
        Toast.makeText(this, getString(R.string.password_editor_message_password_delete),
            Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onPasswordDeletionFailed(
        password: PasswordEntity, message: String?, fragment: WeakReference<Fragment>
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
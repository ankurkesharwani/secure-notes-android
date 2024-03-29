package com.ankur.securenotes.ui.behaviour

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar.SnackbarLayout

class BottomNavigationFABBehavior(context: Context?, attrs: AttributeSet?) :
  CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {

  override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
    return dependency is SnackbarLayout
  }

  override fun onDependentViewRemoved(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View) {
    child.translationY = 0.0f
  }

  override fun onDependentViewChanged(
    parent: CoordinatorLayout, child: FloatingActionButton, dependency: View
  ): Boolean {
    return updateButton(child, dependency)
  }

  private fun updateButton(child: View, dependency: View): Boolean {
    return if (dependency is SnackbarLayout) {
      val oldTranslation = child.translationY
      val height = dependency.getHeight().toFloat()
      val newTranslation = dependency.getTranslationY() - height
      child.translationY = newTranslation
      oldTranslation != newTranslation
    } else {
      false
    }
  }
}
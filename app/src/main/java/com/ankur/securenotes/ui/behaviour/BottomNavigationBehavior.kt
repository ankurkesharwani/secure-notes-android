package com.ankur.securenotes.ui.behaviour

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar.SnackbarLayout

class BottomNavigationBehavior(context: Context, attrs: AttributeSet) :
  CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

  override fun layoutDependsOn(parent: CoordinatorLayout, child: BottomNavigationView, dependency: View): Boolean {
    if (dependency is SnackbarLayout) {
      updateSnackBar(child, dependency)
    }

    return super.layoutDependsOn(parent, child, dependency)
  }

  override fun onStartNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: BottomNavigationView,
    directTargetChild: View,
    target: View,
    axes: Int,
    type: Int
  ): Boolean {
    return axes == ViewCompat.SCROLL_AXIS_VERTICAL
  }

  override fun onNestedPreScroll(
    coordinatorLayout: CoordinatorLayout,
    child: BottomNavigationView,
    target: View,
    dx: Int,
    dy: Int,
    consumed: IntArray,
    type: Int
  ) {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    child.translationY = 0.0f.coerceAtLeast(child.height.toFloat().coerceAtMost(child.translationY + dy))
  }

  private fun updateSnackBar(child: BottomNavigationView, snackBarLayout: SnackbarLayout) {
    if (snackBarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
      if (snackBarLayout.layoutParams == null) {
        throw RuntimeException(
          "null cannot be cast to non-null type android.support.design.widget.CoordinatorLayout.LayoutParams"
        )
      }

      val layoutParams = snackBarLayout.layoutParams
      val params = layoutParams as CoordinatorLayout.LayoutParams
      params.anchorId = child.id
      params.anchorGravity = Gravity.TOP
      params.gravity = Gravity.TOP
      snackBarLayout.layoutParams = params
    }
  }
}
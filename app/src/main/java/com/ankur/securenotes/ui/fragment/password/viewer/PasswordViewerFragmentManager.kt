package com.ankur.securenotes.ui.fragment.password.viewer

import android.content.Context
import com.ankur.securenotes.entity.PasswordEntity

interface PasswordViewerFragmentManager {

  var context: Context?
  var password: PasswordEntity?

}
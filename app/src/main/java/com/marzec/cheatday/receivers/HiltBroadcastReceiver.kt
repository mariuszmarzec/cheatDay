package com.marzec.cheatday.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import dagger.android.AndroidInjection

abstract class HiltBroadcastReceiver : BroadcastReceiver() {
  @CallSuper
  override fun onReceive(context: Context, intent: Intent) {
    AndroidInjection.inject(this, context)
  }
}

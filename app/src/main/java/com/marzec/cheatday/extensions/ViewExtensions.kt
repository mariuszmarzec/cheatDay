package com.marzec.cheatday.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE

fun View.gone() {
    this.visibility = GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = INVISIBLE
}

fun View.setVisible(isVisible: Boolean, invisibilityMode: Boolean = false) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        if (invisibilityMode) INVISIBLE else GONE
    }
}

internal inline fun <reified T : View> View.findParentInstance() : T {
    var parent: View = this
    do {
        parent = parent.parent as View
    } while (parent !is T)
    return parent
}

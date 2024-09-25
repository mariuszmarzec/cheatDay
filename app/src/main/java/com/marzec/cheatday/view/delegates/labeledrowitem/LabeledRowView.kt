package com.marzec.cheatday.view.delegates.labeledrowitem

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.marzec.cheatday.R
import com.marzec.cheatday.extensions.attr

class LabeledRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val label: TextView
    private val value: TextView

    init {
        inflate(context, R.layout.view_labeled_row, this)

        label = findViewById(R.id.label)
        value = findViewById(R.id.value)

        setBackgroundResource(attr(android.R.attr.selectableItemBackground).resourceId)
        orientation = HORIZONTAL
        setPadding(context.resources.getDimensionPixelSize(R.dimen.default_padding))
    }

    fun setValue(valueString: CharSequence) {
        value.text = valueString
    }

    fun setLabel(labelValue: CharSequence) {
        label.text = labelValue
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        super.setOnLongClickListener(l)
    }
}

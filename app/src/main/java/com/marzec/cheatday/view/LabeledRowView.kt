package com.marzec.cheatday.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.marzec.cheatday.R
import kotlinx.android.synthetic.main.view_labeled_row.view.*
import org.jetbrains.anko.attr

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LabeledRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_labeled_row, this)
        setBackgroundResource(attr(R.attr.selectableItemBackground).resourceId)
        orientation = HORIZONTAL
        setPadding(context.resources.getDimensionPixelSize(R.dimen.default_padding))
    }

    @TextProp
    fun setValue(valueString: CharSequence) {
        value.text = valueString
    }

    @TextProp
    fun setLabel(labelValue: CharSequence) {
        label.text = labelValue
    }

    @ModelProp(options = [ModelProp.Option.DoNotHash])
    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
    }
}
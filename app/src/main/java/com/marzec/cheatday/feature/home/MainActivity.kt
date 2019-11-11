package com.marzec.cheatday.feature.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marzec.cheatday.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        valueCounter.onDecreaseButtonClickListener = {
            valueCounter.setValue(it.dec())
        }
        valueCounter.onIncreaseButtonClickListener = { value: Int, max: Int ->
            valueCounter.setValue(value.inc())
        }
    }
}

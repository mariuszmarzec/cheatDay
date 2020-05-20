package com.marzec.cheatday.feature.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseActivity
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterFragment
import com.marzec.cheatday.feature.home.weights.WeightsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceFragment<DaysCounterFragment>()
        }

        bottomNavigationBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.action_home -> {
                    replaceFragment<DaysCounterFragment>()
                    true
                }
                R.id.action_weight -> {
                    replaceFragment<WeightsFragment>()
                    true
                }
                else -> false
            }
        }
    }

    private inline fun <reified F: Fragment> replaceFragment() {
        val tag = F::class.java.simpleName
        val fragment: Fragment =
            supportFragmentManager.findFragmentByTag(tag) ?: F::class.java.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.daysCounterFragment, fragment, tag)
            .commit()
    }
}

package com.marzec.cheatday.common

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import com.marzec.cheatday.HiltTestActivity
import com.marzec.cheatday.R

inline fun <reified T : Fragment> launchFragmentInDaggerContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    crossinline action: Fragment.() -> Unit = {}
): ActivityScenario<HiltTestActivity> {
    val themeExtrasKey = "androidx.fragment.app.testing.FragmentScenario" +
            ".EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY"

    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(themeExtrasKey, themeResId)

    val scenario = ActivityScenario.launch<HiltTestActivity>(startActivityIntent)
    scenario.onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader!!),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }
    return scenario
}

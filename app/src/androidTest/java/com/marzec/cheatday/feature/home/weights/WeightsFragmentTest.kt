package com.marzec.cheatday.feature.home.weights

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.facebook.testing.screenshot.Screenshot
import com.marzec.cheatday.R
import com.marzec.cheatday.common.SingleLiveEvent
import com.marzec.cheatday.common.launchFragmentInDaggerContainer
import com.marzec.cheatday.di.TestViewModelFactoryModule
import com.marzec.cheatday.extensions.liveDataOf
import com.marzec.cheatday.view.model.LabeledRowItem
import com.marzec.cheatday.view.model.ListItem
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.InternalCoroutinesApi
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
/**
 * Screenshottests device - Pixel 3 API 29
 */
class WeightsFragmentTest {

    val viewModel = mock<WeightsViewModel>()

    @Before
    fun setUp() {
        val viewModelFactory = TestViewModelFactoryModule.viewModelFactory
        whenever(viewModelFactory.create<WeightsViewModel>(any())) doReturn viewModel
        whenever(viewModel.goToAddResultScreen) doReturn SingleLiveEvent()
    }

    @InternalCoroutinesApi
    @Test
    fun weights_are_present_on_list() {
        lateinit var activity: FragmentActivity

        whenever(viewModel.list) doReturn liveDataOf(
            listOf<ListItem>(
                LabeledRowItem(
                    id = "id",
                    data = Unit,
                    label = "label",
                    value = "value"
                ),
                LabeledRowItem(
                    id = "id2",
                    data = Unit,
                    label = "label2",
                    value = "value2"
                )
            )
        )

        val scenario = launchFragmentInDaggerContainer<WeightsFragment>(
            themeResId = R.style.AppTheme
        ) {
            WeightsFragment().apply {
                vmFactory = TestViewModelFactoryModule.createFactoryMock()
            }
        }
        scenario.onActivity { activity = it }
        scenario.moveToState(Lifecycle.State.RESUMED)

        Screen.onScreen<WeightsScreen> {
            recyclerView {
                firstChild<Item> {
                    isVisible()
                    value { hasText("value") }
                    label { hasText("label") }
                }
                childAt<Item>(1) {
                    isVisible()
                    value { hasText("value2") }
                    label { hasText("label2") }
                }
            }
        }

        Screenshot.snapActivity(activity)
            .setName("${WeightsFragment::class.java.simpleName}_weights_are_present_on_list")
            .record();
    }
}

class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
    val label: KTextView = KTextView(parent) { withId(R.id.label) }
    val value: KTextView = KTextView(parent) { withId(R.id.value) }
}

class WeightsScreen : Screen<WeightsScreen>() {
    val fab = KImageView { withId(R.id.floatingButton) }
    val recyclerView = KRecyclerView({
        withId(R.id.recyclerView)
    }, itemTypeBuilder = {
        itemType(::Item)
    })
}
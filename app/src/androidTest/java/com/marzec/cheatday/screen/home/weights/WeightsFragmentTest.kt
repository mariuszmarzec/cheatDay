package com.marzec.cheatday.screen.home.weights

import android.view.View
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.marzec.cheatday.R
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher


@HiltAndroidTest
/**
 * Screenshottests device - Pixel 3 API 29
 */
class WeightsFragmentTest {

//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)

//    @Test
//    fun weights_are_present_on_list() {
//
//        whenever(viewModel.list) doReturn liveDataOf(
//            listOf<ListItem>(
//                LabeledRowItem(
//                    id = "id",
//                    data = Unit,
//                    label = "label",
//                    value = "value"
//                ),
//                LabeledRowItem(
//                    id = "id2",
//                    data = Unit,
//                    label = "label2",
//                    value = "value2"
//                )
//            )
//        )
//
//        val scenario = launchFragmentInDaggerContainer<WeightsFragment>(
//            themeResId = R.style.AppTheme
//        )
//        lateinit var activity: HiltTestActivity
//        scenario.onActivity {
//            activity = it
//        }
//        Screen.onScreen<WeightsScreen> {
//            recyclerView {
//                firstChild<Item> {
//                    isVisible()
//                    value { hasText("value") }
//                    label { hasText("label") }
//                }
//                childAt<Item>(1) {
//                    isVisible()
//                    value { hasText("value2") }
//                    label { hasText("label2") }
//                }
//            }
//        }
//        Screenshot.snapActivity(activity)
//            .setName("${WeightsFragment::class.java.simpleName}_weights_are_present_on_list")
//            .record();
//    }
}

class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
    val label: KTextView = KTextView(parent) { withId(R.id.label) }
    val value: KTextView = KTextView(parent) { withId(R.id.value) }
}

class WeightsScreen : Screen<WeightsScreen>() {
    val fab = KImageView { withId(R.id.floating_button) }
    val recyclerView = KRecyclerView({
        withId(R.id.recycler_view)
    }, itemTypeBuilder = {
        itemType(::Item)
    })
}
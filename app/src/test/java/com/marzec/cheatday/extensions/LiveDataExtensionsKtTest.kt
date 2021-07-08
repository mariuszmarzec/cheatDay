package com.marzec.cheatday.extensions

import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.core.values
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class])
internal class LiveDataExtensionsKtTest {

    @Test
    fun map() {
        val liveData1 = MutableLiveData<Int>()
        val liveData2 = liveData1.map { it.toString() }

        val values = liveData2.values()
        liveData1.value = 1
        liveData1.value = 2

        assertThat(values).isEqualTo(listOf("1", "2"))
    }
}
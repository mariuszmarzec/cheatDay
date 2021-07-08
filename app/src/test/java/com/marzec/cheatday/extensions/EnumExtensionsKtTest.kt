package com.marzec.cheatday.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class EnumExtensionsKtTest {

    enum class TestEnum {
        ONE, TWO, THREE
    }

    @Test
    fun enumValueOfOrDefault() {
        assertThat(enumValueOfOrDefault("ONE", TestEnum.ONE)).isEqualTo(TestEnum.ONE)
        assertThat(enumValueOfOrDefault("TWO", TestEnum.TWO)).isEqualTo(TestEnum.TWO)
        assertThat(enumValueOfOrDefault("THREE", TestEnum.THREE)).isEqualTo(TestEnum.THREE)
    }

    @Test
    fun enumValueOfOrDefaultReturnsDefault() {
        assertThat(enumValueOfOrDefault("FOUR", TestEnum.ONE)).isEqualTo(TestEnum.ONE)
    }
}
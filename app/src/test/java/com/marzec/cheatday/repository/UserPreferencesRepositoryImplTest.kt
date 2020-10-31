package com.marzec.cheatday.repository

import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.marzec.cheatday.TestSchedulersRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestSchedulersRule::class)
class UserPreferencesRepositoryImplTest {

    val sharedPreferences: RxSharedPreferences = mock()
    val preference: Preference<Float> = mock()
    val prefSubject = BehaviorSubject.create<Float>()

    lateinit var repository: UserPreferencesRepository

    @BeforeEach
    fun setUp() {
        whenever(sharedPreferences.getFloat("weight")).thenReturn(preference)
        whenever(preference.asObservable()).thenReturn(prefSubject)
        repository = UserPreferencesRepositoryImpl(sharedPreferences, mock())
    }

    @Test
    fun setTargetWeight() = runBlocking {
        repository.setTargetWeight(85.0f)
        verify(sharedPreferences).getFloat("weight")
        verify(preference).set(85.0f)
    }

    @Test
    fun obserTargetWeight() {
        prefSubject.onNext(85f)
        repository.observeTargetWeight().test()
            .assertValue(85.0f)
        verify(sharedPreferences).getFloat("weight")
        verify(preference).asObservable()
    }
}
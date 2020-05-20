package com.marzec.cheatday.interactor

import com.marzec.cheatday.repository.TargetWeightRepository
import com.marzec.cheatday.repository.WeightResultRepository
import javax.inject.Inject

interface WeightInteractor {

}

class WeightInteractorImpl @Inject constructor(
    private val targetRepository: TargetWeightRepository,
    private val weightResultRepository: WeightResultRepository
) : WeightInteractor {

    fun observeTargetWeight() = targetRepository.observeTargetWeight()

    fun updateTargetWeight(weight: Float) = targetRepository.setTargetWeight(weight)
}
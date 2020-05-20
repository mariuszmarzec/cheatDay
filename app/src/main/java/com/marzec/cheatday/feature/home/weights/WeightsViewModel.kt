package com.marzec.cheatday.feature.home.weights

import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.repository.TargetWeightRepository
import javax.inject.Inject

class WeightsViewModel @Inject constructor(
    private val weightRepository: TargetWeightRepository
) : BaseViewModel() {

}

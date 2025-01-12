package com.marzec.featuretoggle

import com.marzec.cheatday.api.request.NewFeatureToggleDto
import com.marzec.cheatday.api.request.UpdateFeatureToggleDto
import com.marzec.cheatday.api.response.FeatureToggleDto
import com.marzec.repository.CrudRepository

typealias FeatureToggleRepository = CrudRepository<Int, FeatureToggle, NewFeatureToggle, UpdateFeatureToggle, FeatureToggleDto, NewFeatureToggleDto, UpdateFeatureToggleDto>

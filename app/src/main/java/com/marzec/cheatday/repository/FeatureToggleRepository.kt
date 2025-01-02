package com.marzec.cheatday.repository

import com.marzec.cheatday.api.request.NewFeatureToggleDto
import com.marzec.cheatday.api.request.UpdateFeatureToggleDto
import com.marzec.cheatday.api.response.FeatureToggleDto
import com.marzec.cheatday.model.domain.FeatureToggle
import com.marzec.cheatday.model.domain.NewFeatureToggle
import com.marzec.cheatday.model.domain.UpdateFeatureToggle
import com.marzec.repository.CrudRepository

typealias FeatureToggleRepository = CrudRepository<Int, FeatureToggle, NewFeatureToggle, UpdateFeatureToggle, FeatureToggleDto, NewFeatureToggleDto, UpdateFeatureToggleDto>

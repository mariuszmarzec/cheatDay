package com.marzec.cheatday.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(mapper: (X) -> Y) : LiveData<Y> = Transformations.map(this, mapper)

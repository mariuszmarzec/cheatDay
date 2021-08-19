package com.marzec.cheatday.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)
}

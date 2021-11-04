package com.marzec.cheatday.screen.login.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.LoginRepository
import com.marzec.mvi.State
import com.marzec.mvi.StoreViewModel
import com.marzec.mvi.reduceContentNoChanges
import com.marzec.mvi.reduceData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    initialState: State<LoginData>
) : StoreViewModel<State<LoginData>, LoginSideEffects>(
    initialState
) {

    fun onLoginButtonClicked() = intent<Content<User>> {
        onTrigger(isCancellableFlowTrigger = true) {
            state.ifDataAvailable {
                loginRepository.login(login, password)
                    .cancelFlowsIf { it is Content.Data }
            }
        }

        reducer {
            state.reduceContentNoChanges(resultNonNull())
        }

        emitSideEffect {
            (resultNonNull() as? Content.Data)?.let { LoginSideEffects.OnLoginSuccessful }
        }
    }

    fun onLoginChanged(login: String) = intent<Unit> {
        reducer {
            state.reduceData {
                copy(login = login)
            }
        }
    }

    fun onPasswordChanged(password: String) = intent<Unit> {
        reducer {
            state.reduceData {
                copy(password = password)
            }
        }
    }
}

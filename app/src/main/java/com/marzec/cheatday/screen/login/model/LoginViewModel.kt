package com.marzec.cheatday.screen.login.model

import com.marzec.content.Content
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
) : StoreViewModel<State<LoginData>, Unit>(
    initialState
) {

    fun onLoginButtonClicked() = intent<Content<User>>("login") {
        onTrigger {
            state.ifDataAvailable {
                loginRepository.login(login, password)
            }
        }

        cancelTrigger(runSideEffectAfterCancel = true) {
            resultNonNull() is Content.Data
        }

        reducer {
            state.reduceContentNoChanges(resultNonNull())
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

package com.marzec.cheatday.screen.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.home.model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView)

        navHostFragment?.let { fragment ->
            NavigationUI.setupWithNavController(
                bottomNavigationView,
                fragment.findNavController()
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect { state ->
                    val isUserLogged = state.isUserLogged
                    bottomNavigationView.isVisible = isUserLogged ?: true
                    val controller = navHostFragment?.findNavController()
                    controller?.let {

                        if (isUserLogged == false) {
                            if (it.currentDestination?.id != R.id.login) {
                                it.navigate(R.id.login)
                            }
                        } else {
                            if (controller.currentDestination?.id == R.id.login) {
                                it.navigateUp()
                            }
                        }
                    }
                }
            }
        }

        viewModel.loadState()
    }
}

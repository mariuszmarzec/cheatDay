package com.marzec.cheatday.screen.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.home.model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.state.observe(this) { state ->
            val isUserLogged = state.isUserLogged
            bottomNavigationView.isVisible = isUserLogged
            if (!isUserLogged) {
                navHostFragment?.findNavController()?.let { controller ->
                    val options = NavOptions.Builder()
                        .apply {
                            controller.currentDestination?.id?.let {
                                setPopUpTo(it, true)
                            }
                        }
                        .build()
                    controller.navigate(R.id.login, null, options)
                }
            }
        }

        viewModel.loadState()
    }
}

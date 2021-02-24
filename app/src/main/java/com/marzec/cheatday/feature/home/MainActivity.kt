package com.marzec.cheatday.feature.home

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseActivity
import com.marzec.cheatday.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var vmFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView)


        val viewModel = vmFactory.create(this).create(MainViewModel::class.java)

        navHostFragment?.let { fragment ->
            NavigationUI.setupWithNavController(
                bottomNavigationView,
                fragment.findNavController()
            )
        }

        viewModel.isUserLogged.observe(this) { isUserLogged ->
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
    }
}

package com.example.myapplication.presentation.navigation

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.di.BankViewModelFactory
import com.example.myapplication.presentation.common.UiState
import com.example.myapplication.presentation.login.*
import com.example.myapplication.presentation.home.*
import com.example.myapplication.presentation.detail.*
import com.example.myapplication.presentation.session.SessionViewModel

@Composable
internal fun BankNavHost(factory: BankViewModelFactory) {
    val nav = rememberNavController()
    val session: SessionViewModel = viewModel(factory = factory)
    val authenticated by session.authenticated.collectAsStateWithLifecycle()

    // A session survives rotation, but is never restored after process death.
    LaunchedEffect(authenticated) {
        if (!authenticated) {
            nav.navigate("login") {
                popUpTo(nav.graph.id) { inclusive = true }
                launchSingleTop = true
            }
        } else if (nav.currentDestination?.route == "login") {
            nav.navigate("home") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = nav, startDestination = "login", modifier = Modifier.safeDrawingPadding()) {
        composable("login") {
            val model: LoginViewModel = viewModel(factory = factory)
            val form by model.form.collectAsStateWithLifecycle()
            val state by model.login.collectAsStateWithLifecycle()
            LoginScreen(form, state, model::emailChanged, model::passwordChanged, model::signIn)
        }
        composable("home") {
            if (authenticated) {
                val model: HomeViewModel = viewModel(factory = factory)
                val state by model.account.collectAsStateWithLifecycle()
                LaunchedEffect(Unit) {
                    if (state is UiState.Idle) model.loadAccount()
                }
                HomeScreen(
                    state = state,
                    retry = { model.loadAccount() },
                    simulate = { model.loadAccount(true) },
                    logout = session::logout,
                    open = { nav.navigate("movement/$it") }
                )
            }
        }
        composable("movement/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) { entry ->
            if (authenticated) {
                val model: DetailViewModel = viewModel(factory = factory)
                val state by model.detail.collectAsStateWithLifecycle()
                val id = entry.arguments?.getString("id").orEmpty()
                LaunchedEffect(id) {
                    if (state is UiState.Idle) model.loadDetail(id)
                }
                DetailScreen(state, { nav.popBackStack() }, { model.loadDetail(id) })
            }
        }
    }
}

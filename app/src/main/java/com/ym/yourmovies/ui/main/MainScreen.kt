package com.ym.yourmovies.ui.main

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ym.yourmovies.cm.home.ui.CmHomeContent
import com.ym.yourmovies.gc.home.ui.GcHomeContent
import com.ym.yourmovies.msub.home.ui.MSubHomeContent
import com.ym.yourmovies.utils.models.CategoryItem
import com.ym.yourmovies.ui.settings.SettingsScreen
import com.ym.yourmovies.utils.others.Route

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    startDestination :String,
    navController: NavHostController,
    isLinear: Boolean,
    cellCount: Int,
    onUpdate:() -> Unit,
    cateList :(List<CategoryItem>)->Unit
) {
//    AnimatedNavHost(navController = navController, startDestination = startDestination) {
//        composable(
//            route = Route.CM,
//            enterTransition = { slideInHorizontally() },
//            exitTransition = { slideOutHorizontally() }
//        ) {
//            CmHomeContent(
//                isLinear = isLinear,
//                cellCount = cellCount,
//                cateItems =  cateList
//            )
//        }
//        composable(
//            route = Route.GC,
//            enterTransition = { slideInHorizontally() },
//            exitTransition = { slideOutHorizontally() }
//        ) {
//            GcHomeContent(
//                isLinear = isLinear,
//                cellCount = cellCount,
//                cateItems =  cateList
//            )
//        }
//        composable(
//            route = Route.MSUB,
//            enterTransition = { slideInHorizontally() },
//            exitTransition = { slideOutHorizontally() }
//        ) {
//            MSubHomeContent(
//                isLinear = isLinear,
//                cellCount = cellCount,
//                cateItems =  cateList
//            )
//        }
//        composable(
//            route = Route.SETTINGS,
//            enterTransition = { slideInHorizontally() },
//            exitTransition = { slideOutHorizontally() }
//        ) {
//            SettingsScreen(onUpdate = onUpdate)
//        }
//    }

        NavHost(navController = navController, startDestination = "splash", modifier = modifier) {
            composable("splash"){
                SplashScreen( onSplashFinish = {})
            }
            animatedComposable(Route.CM) {

                CmHomeContent(
                    isLinear = isLinear,
                    cellCount = cellCount,
                    cateItems =  cateList
                )
            }

            animatedComposable(Route.GC) {
                GcHomeContent(
                    isLinear = isLinear,
                    cellCount = cellCount,
                    cateItems =  cateList
                )
            }
            animatedComposable(Route.MSUB) {
                MSubHomeContent(
                    isLinear = isLinear,
                    cellCount = cellCount,
                    cateItems =  cateList
                )
            }
            composable(Route.SETTINGS){
                SettingsScreen(onUpdate = onUpdate, onback = {})
            }
        }
}

inline fun NavGraphBuilder.animatedComposable(
    route:String,
    crossinline content:@Composable ()->Unit
) {
   composable(route=route){
       AnimatedVisibility(visible = true, enter = fadeIn()) {
           content()
       }
   }
}
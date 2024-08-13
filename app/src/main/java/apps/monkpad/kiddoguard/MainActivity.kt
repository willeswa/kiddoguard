package apps.monkpad.kiddoguard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import apps.monkpad.kiddoguard.data.AppPreferences
import apps.monkpad.kiddoguard.models.AppInfo
import apps.monkpad.kiddoguard.presenter.ui.screens.AppSelectionScreen
import apps.monkpad.kiddoguard.presenter.ui.screens.HomeScreen
import apps.monkpad.kiddoguard.presenter.ui.theme.KiddoGuardTheme
import apps.monkpad.kiddoguard.utils.getInstalledApps
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if KiddoGuard is the default launcher, and if not, open the settings
        if (!isKiddoGuardDefaultLauncher()) {
            openLauncherSettings()
        }

        enableImmersiveMode()
        dimScreenBrightness()

        setContent {
            KiddoGuardTheme {

                var loading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    loading = false // Set loading to false once apps are loaded
                }

                // Set up the NavController
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onAppClick = { app -> launchApp(app) },
                            onNavigateToAppSelection = { navController.navigate("app_selection") },
                            onExitBabyMode = { clearDefaultLauncherAndExit() },
                        )
                    }
                    composable("app_selection") {
                        AppSelectionScreen(

                            navController = navController
                        )
                    }
                }
            }
        }


    }

    override fun onBackPressed() {

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // Re-enable immersive mode whenever window gains focus
            enableImmersiveMode()

        }
    }

    private fun isKiddoGuardDefaultLauncher(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName == packageName
    }

    private fun openLauncherSettings() {
        // Open the settings to let the user select the default launcher
        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        startActivity(intent)

        // Optionally finish the activity after opening settings
        finish()
    }

    private fun clearDefaultLauncherAndExit() {
        // Clear KiddoGuard as the default launcher
        packageManager.clearPackagePreferredActivities(packageName)

        // Finish the activity to exit KiddoGuard
        finish()
    }

    private fun launchApp(app: AppInfo) {
        val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(app.packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            // Handle the case where the app cannot be launched
        }
    }

    private fun enableImmersiveMode() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }

    private fun dimScreenBrightness() {
        // Adjust the screen brightness to 30% of the maximum level
        val layoutParams = window.attributes
        layoutParams.screenBrightness = 0.01f // Adjust this value for desired brightness
        window.attributes = layoutParams
    }



}

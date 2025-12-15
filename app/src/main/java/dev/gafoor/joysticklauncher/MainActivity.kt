@file:OptIn(ExperimentalMaterial3Api::class)

package dev.gafoor.joysticklauncher

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.request.ImageRequest
import dev.gafoor.joysticklauncher.secondscreen.SecondScreen
import dev.gafoor.joysticklauncher.ui.theme.JoystickLauncherTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "homeScreen") {
                composable("homeScreen") { LauncherContent(navController) }
                composable("secondScreen") { SecondScreen() }
            }
            val context = LocalContext.current
            requestNotificationListenerPermission(context)
//            LockScreenStyleNotifications(notificationList)
//                    LauncherContent()
        }
    }


}

@Composable
fun LauncherContent(navController: NavHostController) {
    var drawerVisible by remember { mutableStateOf(false) }


    JoystickLauncherTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        navController.navigate("secondScreen")
                    }
                )
            },
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,

        ) {

            // TimeDisplay()
            // Greeting("Android")
//                    HomeScreenContent()
//                    AppDrawerScreen()
//                     MyLauncher()
            MainScreen(
                drawerVisible,
                navigateToDrawer = {
                    drawerVisible = true
                },
                navigateOutOfDrawer = {
                    drawerVisible = false
                }
            )

            BackHandler(enabled = drawerVisible) {
                drawerVisible = false
            }
            BackHandler(enabled = !drawerVisible) {
                // eat 5 * do nothing
            }
        }
    }
}

private fun requestNotificationListenerPermission(context: Context) {
    val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
    context.startActivity(intent)

}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AppDrawer : Screen("app_drawer")
}

@Composable
fun MyLauncher() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val packageManager = context.packageManager

    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { appInfo ->
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM )  == 0
        }

    LaunchedEffect(Unit) {
        // Preload app icons
        installedApps.forEach { appInfo ->
            val iconUri = Uri.parse("android.resource://" + packageManager.getApplicationInfo(appInfo.packageName, 0).packageName + "/" + appInfo.icon)
            val request = ImageRequest.Builder(context)
                .data(iconUri)
                .allowHardware(false) // Optional
                .build()
            ImageLoader(context).enqueue(request)
        }
    }

    Scaffold(
//        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                text = { Text("Show bottom sheet") },
//                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
//                onClick = {
//
//                }
//            )
//        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dy ->
                        if (dy < 0) {
                            showBottomSheet = true
                        }
                    }
                }
        ) {
            // Screen content
//            HomeScreenContent()
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier.padding(top = 45.dp),

            ) {
                // Sheet content
//                AppDrawerScreen()
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }) {
                    Text("Hide bottom sheet")
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JoystickLauncherTheme {
        Greeting("Android")
    }
}

@Composable
fun TimeDisplay() {
    val currentTime = remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            currentTime.value = LocalDateTime.now()
            delay(1000) // Update every second
        }
    }

    Text(
        text = currentTime.value.format(DateTimeFormatter.ofPattern("hh:mm")),
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Preview
@Composable
fun TimeDisplayPreview() {
    TimeDisplay()
}




private fun switchToVibrateMode(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
}


private fun toggleSystemMode(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

        when (uiModeManager.nightMode) {
            UiModeManager.MODE_NIGHT_NO -> {
                Toast.makeText(context,"l1",Toast.LENGTH_SHORT).show()
                // Switch to dark mode
                uiModeManager.nightMode = UiModeManager.MODE_NIGHT_YES
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
            }
            UiModeManager.MODE_NIGHT_YES -> {
                Toast.makeText(context,"l2",Toast.LENGTH_SHORT).show()
                // Switch to light mode
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)

            }
            else -> {
                Toast.makeText(context,"l3",Toast.LENGTH_SHORT).show()
                // Switch to light mode
                uiModeManager.nightMode = UiModeManager.MODE_NIGHT_YES
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)


            }
        }
    } else {
        // For devices running Android 9 (API level 28) or lower
        val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
        context.startActivity(intent)
    }
}

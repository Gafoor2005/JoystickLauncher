package dev.gafoor.joysticklauncher

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import dev.gafoor.joysticklauncher.appdrawer.AppDrawerScreen
import dev.gafoor.joysticklauncher.home.HomeScreenContent
import dev.gafoor.joysticklauncher.home.NotificationShadeService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    drawerVisible: Boolean,
    navigateToDrawer: () -> Unit,
    navigateOutOfDrawer: () -> Unit
) {


    // drawer code
    val context = LocalContext.current
    val packageManager = context.packageManager


    val installedApps = remember {
        packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            0
        )
            .map { it.activityInfo.applicationInfo }
            .sortedBy { appInfo ->
                appInfo.loadLabel(packageManager).toString()
            }
            .distinctBy { it.packageName }
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






    Box {

        val configuration = LocalConfiguration.current
        val screenHeightDp = configuration.screenHeightDp
        val screenHeightPx = with(LocalDensity.current) { screenHeightDp.dp.toPx() }

        val offsetY = remember { Animatable(0f) }
        val coroutineScope = rememberCoroutineScope()
        var isDragging by remember { mutableStateOf(false) }

        val draggableState = rememberDraggableState { delta ->
            isDragging = true
            coroutineScope.launch {
                offsetY.snapTo((offsetY.value + delta*.5f).coerceIn(-400f, 0f))
            }
        }

        LaunchedEffect(isDragging) {
            if (!isDragging) return@LaunchedEffect

        }

        AnimatedVisibility(
            visible = !drawerVisible,
            enter = slideInVertically(initialOffsetY = { -200 }, animationSpec = tween(durationMillis = 300)) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -200 }, animationSpec = tween(durationMillis = 300)) + fadeOut(),
            modifier = Modifier
                .graphicsLayer { this.translationY = offsetY.value * .75f }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Vertical,
                    onDragStopped = { velocity ->

                        if(velocity > 100 && offsetY.value == 0f){
                            openNotifications(context)
                        }
                        coroutineScope.launch {
                            val triggerDrawer = if (velocity < 0 && offsetY.value < -100f) {
                                true
                            } else {
                                false
                            }
                            if (triggerDrawer) {

                                navigateToDrawer()
                                offsetY.animateTo(-400f, initialVelocity = velocity)
                                offsetY.snapTo(0f)

                            }else {
                                offsetY.animateTo(0f, animationSpec = tween(300))
                            }
                            isDragging = false
                        }
                    }
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            val packageName = "com.coloros.onekeylockscreen" // Replace with the actual package name
                            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
                            if (intent != null) {
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle exceptions, e.g., app not installed
                                    println("Error launching app: ${e.message}")
                                }


                            } else {
                                // Handle case where the app is not installed
                                println("Locker app not found")
                            }
                        }
                    )
                }

        ) {
            Box() {
                HomeScreenContent(packageManager,installedApps = installedApps,navigateToDrawer = navigateToDrawer)
            }
        }
        // Drawer code
        AnimatedVisibility(
            visible = drawerVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            ),

        ) {
            Box(
            ) {
                AppDrawerScreen(
                    context,
                    packageManager,
                    installedApps,
                    draggableState,
                    navigateOutOfDrawer
                )
            }
        }
    }
}

private fun openNotifications(context: Context){
    if (NotificationShadeService.instance != null) {
        NotificationShadeService.instance?.expandNotifications()
    } else {
        // Request Accessibility Permission if the service is not connected
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }
}


@Composable
fun MyScreen(navController: NavController) {
    var visible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        // Content of your screen
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.Yellow),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "My Screen")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {

                visible = false
            }) {
                Text(text = "Navigate")
            }
        }
    }
    AnimatedVisibility(
        visible = !visible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        // Content of the next screen
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Next Screen")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {

                visible = true
            }) {
                Text(text = "go back")
            }
        }
    }
}

@Composable
fun NextScreen(navController: NavController) {
    var visible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        // Content of the next screen
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Next Screen")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                scope.launch {
                    delay(300)
                    navController.popBackStack()
                }
                visible = false
            }) {
                Text(text = "go back")
            }
        }
    }
}
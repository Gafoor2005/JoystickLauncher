package dev.gafoor.joysticklauncher.home

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.gafoor.joysticklauncher.appdrawer.AppIcon
import kotlinx.coroutines.launch


@Composable
fun Joystick(
    packageManager: PackageManager,
    quickApps: List<ApplicationInfo>
){
    val isDarkMode = isSystemInDarkTheme()
    val offsetZ = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

    val boundaryValues = listOf(1f, 22.5f, 45f, 67.5f, 91f)
    var currentRegion by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = offsetZ.value) {
        val newRegion = boundaryValues.indexOfFirst { it > offsetZ.value }
        if (newRegion != currentRegion) {
            val effect = VibrationEffect.createOneShot(
                50,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
            vibratorManager.defaultVibrator.vibrate(effect)
            currentRegion = newRegion
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.dp.toPx() }

    Box(
    ) {

        // background
        Column(
            Modifier
                .fillMaxSize()
                ,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            // area inset
            Box(
                Modifier.graphicsLayer {
                    this.translationX = screenWidthPx - 650f
                    this.translationY = 200f
                },
//                 contentAlignment = Alignment.BottomStart,
            ) {
                // icon
                Column(
                    Modifier.height(280.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        Modifier
                            .size(40.dp)
                            .padding(0.dp)
                    ) {
                        val appInfo = quickApps[3]
                        JoystickIcon(currentRegion==4,appInfo,packageManager)

                    }
                    Column(
                        Modifier
                            .height(140.dp)
                            .graphicsLayer {
                                this.translationX = -150f
                            },
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            Modifier
                                .size(40.dp)
                                .padding(0.dp)
                        ) {
                            val appInfo = quickApps[2]
                            JoystickIcon(currentRegion==3,appInfo,packageManager)

                        }
                        Row(
                            Modifier
                                .size(40.dp)
                                .padding(0.dp)
                        ) {
                            val appInfo = quickApps[1]
                            JoystickIcon(currentRegion==2,appInfo,packageManager)
                        }
                    }
                    Row(
                        Modifier
                            .size(40.dp)
                            .padding(0.dp)
                    ) {
                        val appInfo = quickApps[0]
                        JoystickIcon(currentRegion==1,appInfo,packageManager)

                    }
                }

                // circle
                Box(
                    Modifier
                        .size(280.dp)
                        .background(
                            Color.Black,
                            shape = CircleShape,
                        )
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color.White, Color(0x0B0B0BFF)),
                                start = Offset(0f,0f - offsetZ.value),
                                end = Offset(0f, 900f - offsetZ.value)
                            ),
                            shape = CircleShape,
                        )


                )
            }
        }
        // stick code foreground
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                Modifier.graphicsLayer {
                    this.translationX = screenWidthPx - 650f
                    this.translationY = 200f
                    this.rotationZ = offsetZ.value
                },
                contentAlignment = Alignment.Center,

            ){

                Box(
                    Modifier
                        .size(280.dp)
//                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
//                                    val effect = VibrationEffect.createOneShot(
//                                        50,
//                                        VibrationEffect.DEFAULT_AMPLITUDE
//                                    )
//                                    vibratorManager.defaultVibrator.vibrate(effect)
                                },
                                onDragEnd = {
                                    coroutineScope.launch {
                                        if (currentRegion > 0) {
                                            val launchIntent =
                                                packageManager.getLaunchIntentForPackage(quickApps[currentRegion - 1].packageName)
                                            if (launchIntent != null) {
                                                context.startActivity(launchIntent)
                                            }
                                        }
                                        offsetZ.animateTo(0f, animationSpec = tween(300))
                                    }
                                }
                            ) { change, dragAmount ->
                                coroutineScope.launch {
                                    offsetZ.snapTo(
                                        (offsetZ.value - dragAmount.y * 90f / 280f).coerceIn(
                                            0f,
                                            90f
                                        )
                                    )

                                }
                            }

                        }
                ){
                }
                Box(
                    Modifier
                        .size(50.dp)
                        .graphicsLayer {
                            this.translationX = -200f
                            this.translationY = 200f
                        }
                        .background(

                            Color.White,

                            shape = CircleShape,
                        )

//                        .draggable(
//                            state = rememberDraggableState { delta ->
//                                coroutineScope.launch {
//                                    offsetZ.snapTo((offsetZ.value - delta*.40f).coerceIn(0f,90f))
//                                }
//                            },
//                            Orientation.Vertical,
//                            onDragStarted = {
//                                val effect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
//                                vibratorManager.defaultVibrator.vibrate(effect)
//                            },
//                            onDragStopped = { velocity ->
//                                coroutineScope.launch {
//                                    offsetZ.animateTo(0f, animationSpec = tween(300))
//                                }
//                            }
//                        )
                ){

                }
            }

        }
    }
}

@Composable
fun JoystickIcon(visible:Boolean,appInfo:ApplicationInfo,packageManager: PackageManager){
    AnimatedVisibility(
        visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 100),
            initialOffsetX =  { it / 2 }
        )+ fadeIn(
            animationSpec = tween(durationMillis = 100),
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 200),
            targetOffsetX =  { it / 2 }
        ) + fadeOut(
            animationSpec = tween(durationMillis = 200),
        )
    ) {
        AppIcon(appInfo, packageManager)
    }
}
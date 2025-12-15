package dev.gafoor.joysticklauncher.appdrawer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.PorterDuff
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberAsyncImagePainter
import dev.gafoor.joysticklauncher.R
import kotlinx.coroutines.launch


@Composable
fun AppDrawerScreen(
    context: Context,
    packageManager: PackageManager,
    installedApps: List<ApplicationInfo>,
    draggableState: DraggableState,
    navigateOutOfDrawer: () -> Unit
) {

    val scrollState = rememberLazyListState()
    val viga = FontFamily(Font(R.font.viga_regular))

    val scrollProgress by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex / (installedApps.size - 1).toFloat()
        }
    }
    val maxHeadSize = 700f
    var currentHeadSize by remember { mutableFloatStateOf(maxHeadSize) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection{
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y.toFloat()
                if((delta > 0) && (!scrollState.canScrollBackward)){ // delta is +ve value - dragging down
                    Log.d("scroll delta","backtoo")
                    navigateOutOfDrawer()
                }
                if(delta > 0 ) {
                    if(scrollState.firstVisibleItemIndex == 0)
                        currentHeadSize = maxHeadSize
                    return super.onPreScroll(available, source)
                }
                Log.d("first index",scrollState.firstVisibleItemIndex.toString() +" "+scrollState.firstVisibleItemScrollOffset)
                val newHeadSize = currentHeadSize + delta
                val previousHeadSize = currentHeadSize
                currentHeadSize = newHeadSize.coerceIn(400f,maxHeadSize)
                val consumed = currentHeadSize - previousHeadSize
                return Offset(0f,consumed)
            }
        }
    }

    // Fab setup
    val coroutineScope = rememberCoroutineScope()
    var fabVisible by remember { mutableStateOf(false) }
    val firstItemVisible by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0
        }
    }

    fabVisible = firstItemVisible
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabVisible,
                enter = androidx.compose.animation.fadeIn(animationSpec = tween(500)),
                exit = androidx.compose.animation.fadeOut(animationSpec = tween(500))
            ) {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        // scrollState.animateScrollToItem(0)
                        navigateOutOfDrawer()
                    }
                }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Scroll to top")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {

        innerPadding ->
        Column(
            Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .nestedScroll(nestedScrollConnection)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(currentHeadSize.toInt() * 1.dp)
                    .padding(horizontal = 16.dp)

//                    .padding(bottom = 15.dp)
//                .height(40.dp)
//                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Column {


                    Text(
                        fontSize = 4.sp * (currentHeadSize/100),
                        text = "Applications",
                        fontFamily = viga
                    )
                    LinearProgressIndicator(
                        progress = { scrollProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 4.dp),
                        trackColor = Color.Cyan,
                        color = Color.Yellow,
                        strokeCap = StrokeCap.Square
                    )
                }
            }
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
//                    .padding(top = 16.dp)
                    .padding(end = 4.dp)
                    .draggable(draggableState, orientation = Orientation.Vertical)
            ) {
                item {
//
                    Spacer(modifier = Modifier.height(15.dp))
                }
                items(installedApps) { appInfo ->
                    val appName = appInfo.loadLabel(packageManager).toString()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable {
                                val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
                                if (launchIntent != null) {
                                    context.startActivity(launchIntent)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Transparent),
                        ) {
                            AppIcon(appInfo, packageManager)

                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = appName)
                        }
                    }
                }
                item {
//
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

@Composable
fun AppIcon(appInfo: ApplicationInfo, packageManager: PackageManager) {
    val iconUri = Uri.parse("android.resource://" + packageManager.getApplicationInfo(appInfo.packageName, 0).packageName + "/" + appInfo.icon)

    Image(
        painter = rememberAsyncImagePainter(iconUri),
        contentDescription = appInfo.loadLabel(packageManager).toString(),
    )
}
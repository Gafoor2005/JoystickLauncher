package dev.gafoor.joysticklauncher.home
import android.app.PendingIntent
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import dev.gafoor.joysticklauncher.MyNotificationListenerService
import dev.gafoor.joysticklauncher.NotificationData


@Composable
fun DisplayNotifications(notificationList: List<NotificationData>) {
    val scrollState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .zIndex(0f)
//            .background(Color(0xFF1C1CFF)) // Dark gray iOS-like background
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = 50.dp, // Add padding at the top
                bottom = 50.dp // Add padding at the bottom
            ),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.scaleX = .85f
                    this.scaleY = .85f
                    this.translationY = 70f
                },
            state = scrollState,
//            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            items(notificationList.size) { index ->
                // Use derivedStateOf to calculate visibility

                NotificationCardWithAnimation(notificationList[index], index,scrollState)
            }
        }
    }
}

@Composable
fun NotificationCardWithAnimation(notification: NotificationData, index: Int, scrollState: LazyListState) {

    val context = LocalContext.current
    val packageManager = context.packageManager
    val appName = getAppNameFromPackage(context,notification.appName)

//    // Trigger the visibility when the notification appears
//    LaunchedEffect(notification) {
////        delay(100 * index.toLong()) // Adding a slight delay for each notification
//        isVisible = true
//    }

    var itemHeightPx by remember { mutableStateOf(0) } // Height in pixels


    val alphaValue by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex == index) {
                // Normalize the offset: 1f at the top, 0f when fully scrolled
                1f - (scrollState.firstVisibleItemScrollOffset.toFloat() / itemHeightPx.toFloat()).coerceIn(0f, 1f)
            } else if (scrollState.firstVisibleItemIndex > index) {
                0f // Fully faded out if the item has already scrolled past
            } else if(scrollState.firstVisibleItemIndex == index-1){
                1.5f
            }else if(scrollState.firstVisibleItemIndex == index-2){
                (scrollState.firstVisibleItemScrollOffset.toFloat() / itemHeightPx.toFloat()).coerceIn(0f, 1.5f)
            } else {
                0f // Fully visible if the item hasn't been reached yet
            }
        }
    }
    val scaleValue by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex == index-1) {
                // Normalize the offset: 1f at the top, 0f when fully scrolled
                (scrollState.firstVisibleItemScrollOffset.toFloat() / itemHeightPx.toFloat()).coerceIn(0f, 1f) * .1f + 1.1f
            } else if ((scrollState.firstVisibleItemIndex == index)) {
                (1f - (scrollState.firstVisibleItemScrollOffset.toFloat() / itemHeightPx.toFloat()).coerceIn(0f, 1f)) * .1f + 1.1f
            }   else {
                1.1f // Fully visible if the item hasn't been reached yet
            }
        }
    }
    val offsetY = -(index * 20).dp

    Box(

        modifier = Modifier// Apply the offset to create the stacked effect
//            .background(Color.LightGray)
            .onGloballyPositioned { layoutCoordinates ->
                // Get the height in pixels
                itemHeightPx = layoutCoordinates.size.height
            }.graphicsLayer {
                this.alpha = alphaValue;
                this.scaleX = scaleValue;
                this.scaleY = scaleValue;
            }
    ) {
        // Display the notification card
        Card(
            shape = RoundedCornerShape(10.dp),

//            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary), // iOSM-like background
//            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), // iOSM-like background
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black,
                    spotColor = Color(0xFFF1F1F1)
                ).pointerInput(Unit){
                    detectTapGestures(
                        onTap = {
                            val pendingIntent = notification.pendingIntent
                            try {
                                pendingIntent?.send() // Trigger the action
                            } catch (e: PendingIntent.CanceledException) {
                                e.printStackTrace()
                            }
                            Log.d("notif","${notification.appName}")
//                            notification.clear()
                        }
                    )
                }
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
            ){
                val appInfo = packageManager.getApplicationInfo(notification.appName, 0) // Replace with actual package name
                AppIcon(appInfo = appInfo, packageManager = packageManager, Modifier.size(30.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // App Name
//                        Text(
//                            text = appName,
//                            fontSize = 11.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFF9E9E9E),
//                            lineHeight = 11.sp
//                        )
                        Text(
                            text = notification.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1, // Ensure single line
                            lineHeight = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f) // Make this take available space
                        )
                        // Time
                        Text(
                            text = notification.time,
                            maxLines = 1,
                            fontSize = 11.sp,
                            color = Color(0xFF6E6E6E),
                            lineHeight = 11.sp,
                            modifier = Modifier
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        //                    horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Title
//                        Text(
//                            text = notification.title,
//                            fontSize = 12.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White,
//                            maxLines = 1, // Ensure single line
//                            lineHeight = 14.sp
//                            //                        overflow = TextOverflow.Ellipsis, // Add ellipsis for overflow
//                            //                        modifier = Modifier.weight(1f) // Allow equal space sharing between texts
//                        )

//                        Spacer(modifier = Modifier.width(8.dp)) // Optional spacing between title and content

                        // Content
                        Text(
                            text = notification.content,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1, // Ensure single line
                            overflow = TextOverflow.Ellipsis, // Add ellipsis for overflow
                            modifier = Modifier.weight(1f), // Allow equal space sharing between texts
                            lineHeight = 14.sp
                        )
                    }


                }
            }
        }
    }
}


fun getAppNameFromPackage(context: Context, packageName: String): String {
    return try {
        val packageManager: PackageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val appName = packageManager.getApplicationLabel(applicationInfo)
        appName.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        packageName // Return package name if app name is not found
    }
}
@Composable
fun AppIcon(appInfo: ApplicationInfo, packageManager: PackageManager, modifier: Modifier = Modifier) {
    // Retrieve and remember the app icon as an ImageBitmap
    val iconBitmap: ImageBitmap? = remember {
        val drawable = packageManager.getApplicationIcon(appInfo.packageName)
        drawableToBitmap(drawable)?.asImageBitmap()
    }

    if (iconBitmap != null) {
        // Display the app icon
        Image(
            bitmap = iconBitmap,
            contentDescription = appInfo.loadLabel(packageManager).toString(),
            modifier = modifier
        )
    } else {
        // Placeholder if the icon cannot be loaded
        Image(
            painter = painterResource(id = android.R.drawable.sym_def_app_icon),
            contentDescription = "Default App Icon",
            modifier = modifier
        )
    }
}

// Helper function to convert Drawable to Bitmap
fun drawableToBitmap(drawable: Drawable): Bitmap? {
    return if (drawable.intrinsicWidth > 0 && drawable.intrinsicHeight > 0) {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } else {
        null
    }
}
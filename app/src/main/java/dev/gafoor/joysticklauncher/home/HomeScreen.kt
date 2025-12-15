package dev.gafoor.joysticklauncher.home

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.media.AudioManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gafoor.joysticklauncher.NotificationData
import dev.gafoor.joysticklauncher.R
import dev.gafoor.joysticklauncher.notificationList
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    packageManager: PackageManager,
    installedApps: List<ApplicationInfo>,
    navigateToDrawer: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    val context = LocalContext.current
    val currentTime = remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            currentTime.value = LocalDateTime.now()
            delay(1000L) // Delay for 1 second
        }
    }
    val formattedDate = currentTime.value.format(DateTimeFormatter.ofPattern("d MMM"))
    val formattedHours = currentTime.value.format(DateTimeFormatter.ofPattern("hh"))
    val formattedMinutes = currentTime.value.format(DateTimeFormatter.ofPattern("mm"))

    val blackHanSans = FontFamily(Font(R.font.black_han_sans))
    val viga = FontFamily(Font(R.font.viga_regular))


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(35.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth(),


            ) {
            Box(modifier = Modifier.height(57.dp))
            Text(
                text = formattedHours,
                fontSize = 48.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = blackHanSans,
                textAlign = TextAlign.Center
            )
            Text(
                text = formattedMinutes,
                fontSize = 48.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = blackHanSans,
                textAlign = TextAlign.Right
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = formattedDate,
                lineHeight = 51.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = viga,
                textAlign = TextAlign.Right
            )
            Spacer(modifier = Modifier.height(80.dp))
            Row() {
                Surface(shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(55.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    onClick = {

                        //                    toggleSystemMode(context)
                    }
                ) {
                    // Your content here
                    Box(
                        Modifier.size(24.dp), contentAlignment = Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.light_mode),
                            contentDescription = "light"
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(55.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        toggleVibrateMode(context)
                    }
                ) {
                    // Your content here
                    Box(
                        Modifier.size(24.dp), contentAlignment = Center
                    ) {
                        Image(
                            painter = painterResource(id = if(isDarkMode) R.drawable.sharp_vibration_24 else R.drawable.sharp_vibration_24_white),
                            contentDescription = "vibe"
                        )
                    }
                }


            }


        }
        DisplayNotifications(notificationList)
//        Button(
//            onClick = { navigateToDrawer()}
//            //            onClick = {navController.navigate(Screen.AppDrawer.route)}
//        ) {
//            Text(
//                text = "swipe up for more apps",
//                color = MaterialTheme.colorScheme.secondary,
//                fontFamily = viga,
//                fontSize = 11.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//
//            )
//        }
    }
    // com.whatsapp
    // com.google.android.dialer
    // com.google.android.youtube
    // com.google.android.googlequicksearchbox
    val wantedPackages = listOf(
        "com.whatsapp",
        "com.google.android.dialer",
        "com.google.android.youtube",
        "com.google.android.googlequicksearchbox"
    )

    val quickApps = installedApps.filter { appInfo ->
        wantedPackages.contains(appInfo.packageName)
    }
    Joystick(packageManager, quickApps = quickApps.slice(IntRange(0,3)))

}

@Composable
fun NotificationList() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(notificationList.size) { index ->
            val notification = notificationList[index]
            NotificationItem(notification)
        }
    }
}
@Composable
fun NotificationItem(notification: NotificationData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "App: ${notification.appName}")
        Text(text = "Title: ${notification.title}")
        Text(text = "Text: ${notification.content}")
    }
}

private fun toggleVibrateMode(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    when (audioManager.ringerMode) {
        AudioManager.RINGER_MODE_NORMAL -> {
            // Switch to vibrate mode
            audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
        }
        AudioManager.RINGER_MODE_VIBRATE -> {
            // Switch to ring mode
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
        else -> {
            // Switch to ring mode
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
    }
}
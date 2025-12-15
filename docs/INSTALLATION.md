# Installation Guide

## System Requirements

### Minimum Requirements
- **Android Version**: Android 8.0 (API Level 26) or higher
- **RAM**: 2GB minimum
- **Storage**: 50MB free space
- **Screen**: Any screen size supported

### Recommended Requirements
- **Android Version**: Android 12 (API Level 31) or higher
- **RAM**: 4GB or more
- **Storage**: 100MB free space
- **Processor**: Quad-core processor or better

## Installation Methods

### Method 1: Install from Release (Recommended)

1. **Download the APK**
   - Navigate to the `app/release/` folder
   - Download `app-release.aab` (Android App Bundle)
   - Note: You may need to extract or convert the .aab file to .apk using bundletool

2. **Enable Unknown Sources**
   - Go to Settings → Security
   - Enable "Install unknown apps" or "Unknown sources"
   - Allow installation from your file manager/browser

3. **Install the App**
   - Open the downloaded APK file
   - Tap "Install"
   - Wait for installation to complete

### Method 2: Build from Source

#### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK with API 34 installed
- Gradle 8.7.3+

#### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/JoystickLauncher.git
   cd JoystickLauncher
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the JoystickLauncher folder
   - Wait for Gradle sync to complete

3. **Configure Build**
   - Ensure you have the required SDK versions installed
   - Go to Tools → SDK Manager
   - Install Android API 34 if not already installed

4. **Build the Project**
   - Go to Build → Make Project
   - Wait for the build to complete
   - Check for any errors in the Build tab

5. **Run on Device/Emulator**
   - Connect your Android device via USB (with USB debugging enabled)
   - OR start an Android emulator
   - Click the "Run" button (green triangle)
   - Select your device
   - Wait for the app to install and launch

6. **Generate Signed APK/Bundle**
   - Go to Build → Generate Signed Bundle / APK
   - Select "Android App Bundle" or "APK"
   - Follow the wizard to sign with your keystore
   - The generated file will be in `app/release/`

## Post-Installation Setup

### 1. Set as Default Launcher

1. Press the Home button
2. Select "Joystick Launcher"
3. Tap "Always" to set as default

OR

1. Go to Settings → Apps → Default apps
2. Select "Home app" or "Launcher"
3. Choose "Joystick Launcher"

### 2. Grant Required Permissions

The app will request the following permissions:

#### Notification Access (Required)
1. When prompted, tap "Allow"
2. OR go to Settings → Apps → Joystick Launcher → Permissions
3. Enable "Notification access"

#### Post Notifications (Android 13+)
1. When prompted, tap "Allow"
2. This enables the app to display notifications

#### Other Permissions
- **Vibrate**: For haptic feedback on joystick interactions
- **Query All Packages**: To display all installed apps in the drawer

### 3. Optimize Battery Settings

To ensure notifications work properly:

1. Go to Settings → Battery → Battery optimization
2. Find "Joystick Launcher"
3. Select "Don't optimize" or "Unrestricted"

## Troubleshooting

### Installation Failed
- **Error: App not installed**
  - Clear the data of the Play Store and try again
  - Ensure you have enough storage space
  - Try installing via ADB: `adb install app-release.apk`

### Can't Set as Default Launcher
- **Solution**: 
  - Restart your device
  - Check if you have another launcher app that's locked as default
  - Clear defaults for your current launcher in Settings

### Notifications Not Showing
- **Solution**:
  1. Go to Settings → Apps → Special app access
  2. Select "Notification access"
  3. Enable "Joystick Launcher"
  4. Restart the app

### App Crashes on Launch
- **Solution**:
  - Clear app data: Settings → Apps → Joystick Launcher → Storage → Clear data
  - Reinstall the app
  - Ensure your Android version meets minimum requirements
  - Check logcat for specific error messages

## Uninstallation

1. Set a different app as default launcher first
2. Go to Settings → Apps → Joystick Launcher
3. Tap "Uninstall"
4. Confirm the uninstallation

## Building for Different Architectures

The app supports all Android architectures:
- armeabi-v7a
- arm64-v8a
- x86
- x86_64

If you need to build for a specific architecture, modify `app/build.gradle`:

```gradle
android {
    splits {
        abi {
            enable true
            reset()
            include 'arm64-v8a', 'armeabi-v7a'
            universalApk false
        }
    }
}
```

## Support

For installation issues, please:
- Check the [Troubleshooting](TROUBLESHOOTING.md) guide
- Open an issue on GitHub
- Contact the developer

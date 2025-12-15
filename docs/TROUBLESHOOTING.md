# Troubleshooting Guide

## Common Issues and Solutions

### Installation Issues

#### "App not installed" error

**Symptoms**: Installation fails with "App not installed" message

**Solutions**:
1. **Check Storage Space**
   - Ensure you have at least 100MB free space
   - Go to Settings → Storage to check

2. **Clear Package Installer Cache**
   - Settings → Apps → Package Installer → Storage → Clear Cache
   - Retry installation

3. **Verify APK Integrity**
   - Download the APK again
   - Ensure download completed fully

4. **Install via ADB**
   ```bash
   adb install -r app-release.apk
   ```

#### "Installation blocked" error

**Cause**: Security settings prevent installation

**Solution**:
1. Go to Settings → Security
2. Enable "Unknown sources" or "Install unknown apps"
3. Allow installation from your file manager/browser

### Launcher Issues

#### Can't set as default launcher

**Symptoms**: Option to set as default doesn't appear

**Solutions**:
1. **Clear Current Launcher Defaults**
   - Settings → Apps → [Current Launcher]
   - Tap "Open by default" → Clear defaults
   - Press Home button and select Joystick Launcher

2. **Manual Selection**
   - Settings → Apps → Default apps → Home app
   - Select "Joystick Launcher"

3. **After Android 10**
   - Some devices hide this option
   - Press Home button, the selection dialog should appear

#### Launcher crashes on startup

**Symptoms**: App closes immediately after opening

**Solutions**:
1. **Clear App Data**
   - Settings → Apps → Joystick Launcher
   - Storage → Clear Storage
   - Relaunch the app

2. **Check Permissions**
   - Ensure all required permissions are granted
   - Especially "Query all packages"

3. **Reinstall**
   - Uninstall the app
   - Restart device
   - Reinstall

4. **Check Device Compatibility**
   - Verify Android version is 8.0+
   - Some custom ROMs may have compatibility issues

5. **Logcat Debugging**
   ```bash
   adb logcat | grep JoystickLauncher
   ```

### Notification Issues

#### Notifications not appearing

**Symptoms**: Notifications from other apps don't show in launcher

**Solutions**:
1. **Grant Notification Access**
   - Settings → Apps → Special app access
   - Notification access → Enable for Joystick Launcher
   - Toggle OFF then ON if already enabled

2. **Check Battery Optimization**
   - Settings → Battery → Battery optimization
   - Find Joystick Launcher
   - Select "Don't optimize"

3. **Restart Notification Service**
   - Disable notification access
   - Force stop the app
   - Re-enable notification access
   - Relaunch app

4. **Android 13+ Permission**
   - Ensure "Post notifications" permission is granted
   - Settings → Apps → Joystick Launcher → Permissions

#### Notifications delayed or missing

**Symptoms**: Notifications appear late or not at all

**Solutions**:
1. **Disable Battery Saver**
   - Battery saver restricts background services
   - Disable it or whitelist Joystick Launcher

2. **Background Restriction**
   - Settings → Apps → Joystick Launcher
   - Battery → Background restriction → Unrestricted

3. **Manufacturer-Specific Settings**
   - **Xiaomi/MIUI**: Settings → Battery → Power saving features → Disable
   - **Huawei/EMUI**: Settings → Battery → App launch → Manual management
   - **OnePlus/OxygenOS**: Settings → Battery → Battery optimization → Don't optimize
   - **Samsung/One UI**: Settings → Device care → Battery → Background usage limits

### Joystick Issues

#### Joystick not responding

**Symptoms**: Joystick doesn't respond to touch/drag

**Solutions**:
1. **Check Touch Sensitivity**
   - Test touch in other apps
   - Clean screen
   - Remove screen protector if interfering

2. **Restart App**
   - Force stop the app
   - Clear from recent apps
   - Relaunch

3. **Reduce Animations**
   - Developer options → Animation scale → 0.5x
   - Or disable animations temporarily

#### Haptic feedback not working

**Symptoms**: No vibration when using joystick

**Solutions**:
1. **Check Vibration Settings**
   - Settings → Sound & vibration → Vibration
   - Ensure vibration is enabled system-wide

2. **Permission**
   - Vibrate permission should be auto-granted
   - Reinstall if necessary

3. **Hardware Issue**
   - Test vibration in other apps
   - Device may have hardware problem

#### Joystick animation laggy

**Symptoms**: Slow or choppy joystick movement

**Solutions**:
1. **Close Background Apps**
   - Free up RAM
   - Close unnecessary apps

2. **Restart Device**
   - Clears memory
   - Resets system services

3. **Hardware Acceleration**
   - Should be enabled by default
   - Check Developer options → Force GPU rendering

### App Drawer Issues

#### Apps not showing in drawer

**Symptoms**: Some or all apps missing from app drawer

**Solutions**:
1. **Grant Query Packages Permission**
   - Required to see all apps
   - Should be auto-granted via manifest
   - Try reinstalling if not working

2. **Refresh App List**
   - Force stop the app
   - Clear app cache
   - Relaunch

3. **System Apps Hidden**
   - Some system apps may not have launcher intents
   - This is normal behavior

#### App icons not loading

**Symptoms**: Apps show but icons are blank or missing

**Solutions**:
1. **Clear Image Cache**
   - Settings → Apps → Joystick Launcher
   - Storage → Clear cache
   - Relaunch app

2. **Network Issues** (if using cloud icons)
   - App uses local icons only
   - Shouldn't require internet

3. **Wait for Preload**
   - Icons preload on first launch
   - May take a few seconds
   - Scroll drawer to force loading

### Performance Issues

#### App runs slowly

**Solutions**:
1. **Clear Cache**
   - Settings → Apps → Joystick Launcher → Storage → Clear cache

2. **Free Up RAM**
   - Close background apps
   - Restart device

3. **Reduce Animations**
   - Developer options → Window/Transition animation scale → 0.5x

4. **Update App**
   - Check for latest version
   - Install updates

#### High battery drain

**Solutions**:
1. **Check Battery Usage**
   - Settings → Battery → Battery usage
   - See what's consuming power

2. **Disable Unnecessary Features**
   - Reduce notification polling if possible

3. **Background Restrictions**
   - Should be set to "Unrestricted" for proper function
   - Balance between battery and functionality

### UI Issues

#### Blank or black screen

**Solutions**:
1. **Restart App**
   - Force stop and relaunch

2. **Clear App Data**
   - Settings → Apps → Joystick Launcher → Storage → Clear data
   - Warning: Resets all settings

3. **GPU Rendering**
   - Developer options → Force GPU rendering → Enable

#### Text or icons too small/large

**Solutions**:
1. **Display Settings**
   - Settings → Display → Display size
   - Settings → Display → Font size
   - Adjust to preference

2. **Accessibility**
   - Settings → Accessibility → Display size and text

#### Dark/Light theme not working

**Solutions**:
1. **System Theme**
   - App follows system theme
   - Settings → Display → Dark theme

2. **Force Restart**
   - Theme changes on app restart
   - Force stop and relaunch

### Permission Issues

#### "Permission denied" errors

**Solutions**:
1. **Check App Permissions**
   - Settings → Apps → Joystick Launcher → Permissions
   - Grant all required permissions

2. **Notification Listener**
   - Settings → Apps → Special app access → Notification access
   - Enable for Joystick Launcher

3. **Android 13+ Permissions**
   - Post notifications must be manually granted
   - App should prompt on first launch

### Device-Specific Issues

#### Samsung devices

**Issues**: Launcher reset after restart, battery optimization

**Solutions**:
- Add to "Never sleeping apps"
- Disable "Adaptive battery"
- Settings → Device care → Battery → Background usage limits → Never sleeping apps

#### Xiaomi/MIUI devices

**Issues**: Aggressive battery optimization, notifications blocked

**Solutions**:
- Settings → Battery & performance → Battery saver → Off
- Settings → Apps → Manage apps → Joystick Launcher
  - Autostart → Enable
  - Battery saver → No restrictions
  - Display pop-up windows → Enable

#### Huawei devices

**Issues**: Background processes killed, no notifications

**Solutions**:
- Settings → Battery → App launch → Manual management
- Settings → Apps → Joystick Launcher → Battery → Allow background activity

#### OnePlus devices

**Issues**: Notifications delayed

**Solutions**:
- Settings → Battery → Battery optimization → Joystick Launcher → Don't optimize
- Settings → Apps → Joystick Launcher → Mobile data & Wi-Fi → Allow background data usage

### Android TV Issues

#### Navigation with remote

**Solutions**:
1. **D-pad Navigation**
   - Use directional pad for navigation
   - OK/Enter button to select

2. **Mouse Mode**
   - Enable mouse pointer in Android TV settings
   - Use remote as mouse

#### Display scaling

**Solutions**:
1. **Adjust TV Display Settings**
   - Settings → Display → Screen resolution
   - Ensure overscan is disabled

2. **App Scaling**
   - Currently uses responsive design
   - Should adapt to TV screens

## Error Messages

### "Failed to load applications"

**Cause**: Permission issue or system error

**Solution**:
1. Grant "Query all packages" permission
2. Restart app
3. Check logcat for detailed error

### "Notification service disconnected"

**Cause**: System killed the notification listener service

**Solution**:
1. Disable battery optimization
2. Re-enable notification access
3. Restart device

### "Cannot launch app"

**Cause**: App uninstalled or disabled

**Solution**:
1. Verify app is still installed
2. Check if app is disabled in Settings
3. Reinstall the target app

## Getting More Help

### Logs for Debugging

If you need to report an issue, collect logs:

```bash
# Install ADB
# Enable USB debugging on device
# Connect device to computer

# Collect logs
adb logcat -d > joystick_launcher_log.txt

# Filter for relevant logs
adb logcat | grep -i "joystick\|launcher"
```

### Information to Include in Bug Reports

1. Device model and manufacturer
2. Android version
3. App version
4. Steps to reproduce
5. Expected vs actual behavior
6. Screenshots or videos
7. Logcat output (if possible)

### Where to Get Help

- **GitHub Issues**: [Report a bug](../../issues/new)
- **Discussions**: [Ask a question](../../discussions)
- **Email**: dev.gafoor@joysticklauncher

## FAQ

### Q: Is internet required?
**A**: No, the app works completely offline.

### Q: Does the app collect data?
**A**: No, zero data collection. See [PRIVACY_POLICY.md](PRIVACY_POLICY.md).

### Q: Can I customize the joystick apps?
**A**: Currently showing default quick apps. Customization planned for future release.

### Q: Why does it need notification access?
**A**: To display notifications in the launcher's notification shade.

### Q: Can I use this on tablet?
**A**: Yes, supports all screen sizes.

### Q: Does it support widgets?
**A**: Not yet, but planned for future release.

### Q: Can I revert to my old launcher?
**A**: Yes, simply set your old launcher as default in Settings → Apps → Default apps.

### Q: Why is battery optimization important to disable?
**A**: Android kills background services to save battery. This prevents notifications from working properly.

---

*If your issue isn't listed here, please [open an issue](../../issues/new) on GitHub.*

# Changelog

All notable changes to Joystick Launcher will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned Features
- Widget support on home screen
- Customizable joystick quick app selection
- App search in drawer
- Icon pack support
- Backup and restore settings
- Custom gesture actions
- Theme customization options

## [1.0.0] - 2024-XX-XX

### Added
- Initial release of Joystick Launcher
- 🕹️ Innovative joystick-based app launcher interface
- 🏠 Minimalist home screen with live clock display
- 📱 Full app drawer with alphabetically sorted apps
- 🔔 Integrated notification listener service
- 🔔 Notification shade with swipe-down gesture
- 🎨 Material Design 3 UI with dark/light theme support
- ⚡ Smooth animations and transitions throughout
- 📳 Haptic feedback on joystick interactions
- 📺 TV mode support for Android TV devices
- 🎯 Quick app access via joystick interface
- 🔄 Real-time clock and date display
- 🎵 Volume indicator integration
- 🌓 Automatic dark/light theme switching
- 📱 Edge-to-edge display support
- 🔒 Complete privacy - zero data collection
- 🚫 No internet required - fully offline operation
- 📦 App icon preloading for smooth performance
- 🎭 Custom fonts support (Black Han Sans, Viga)

### Features in Detail

#### Navigation
- Gesture-based navigation system
- Swipe up to open app drawer
- Swipe down for notification shade
- Tap and hold for secondary screens
- Drag joystick to select apps

#### Home Screen
- Large clock display with custom fonts
- Current date display
- Minimal, distraction-free design
- Quick access to notifications
- System integration (volume, time)

#### App Drawer
- All installed apps displayed
- Alphabetical sorting
- Smooth scrolling with LazyColumn
- App icon caching and preloading
- One-tap app launching

#### Joystick
- Circular app arrangement
- Drag-to-select interface
- Region-based app zones
- Visual feedback on selection
- Haptic vibration on region change
- Smooth animations
- Quick app launch on release

#### Notifications
- Real-time notification monitoring
- Notification shade UI
- Persistent notification list
- Clear/dismiss functionality
- App icon in notifications
- Timestamp display
- Direct notification actions
- Notification listener service

#### Technical
- Built with Jetpack Compose
- Material Design 3 implementation
- MVVM architecture pattern
- Kotlin coroutines for async operations
- Coil for image loading
- Navigation Component for routing
- State management with Compose
- Edge-to-edge window support

### Permissions
- QUERY_ALL_PACKAGES - Display all apps
- BIND_NOTIFICATION_LISTENER_SERVICE - Access notifications
- ACCESS_NOTIFICATION_POLICY - Read notification settings
- POST_NOTIFICATIONS - Display notifications (Android 13+)
- VIBRATE - Haptic feedback

### Supported Android Versions
- Minimum: Android 8.0 (API 26)
- Target: Android 14 (API 34)
- Tested on: Android 8 through Android 14

### Known Issues
- None reported in initial release

### Dependencies
- Kotlin 1.9.22
- Gradle 8.7.3
- Compose BOM (latest)
- Material3
- Navigation Compose
- Coil Compose
- AndroidX Core KTX
- AndroidX Activity Compose
- AndroidX Lifecycle

### Build Info
- Build Tools: 34.0.0
- Compile SDK: 34
- Min SDK: 26
- Target SDK: 34

---

## Version History

### Version Naming Scheme
- **Major.Minor.Patch** (e.g., 1.0.0)
- **Major**: Breaking changes or major feature additions
- **Minor**: New features, backward compatible
- **Patch**: Bug fixes and minor improvements

### Release Cycle
- Major releases: As needed
- Minor releases: Monthly (if features ready)
- Patch releases: As needed for critical bugs

---

## Future Roadmap

### Version 1.1.0 (Planned)
- [ ] App search functionality
- [ ] Quick settings panel
- [ ] Gesture customization
- [ ] Performance improvements

### Version 1.2.0 (Planned)
- [ ] Widget support
- [ ] Custom wallpaper support
- [ ] Icon pack support
- [ ] Theme engine

### Version 2.0.0 (Planned)
- [ ] Complete UI redesign options
- [ ] Advanced customization
- [ ] Backup and restore
- [ ] Cloud sync (optional)
- [ ] Plugin system

---

## How to Report Issues

Found a bug or have a suggestion? Please:
1. Check existing issues
2. Create a new issue with details
3. Include device info and logs
4. Follow the issue template

---

## Credits

### Developer
- **Gafoor** - Initial work and ongoing development

### Libraries & Tools
- Jetpack Compose Team
- Material Design Team
- Coil contributors
- Android Open Source Project

### Inspiration
- Traditional joystick interfaces
- Minimalist launcher designs
- Android TV launchers

---

*For detailed changes and commits, see the [Git history](../../commits/main).*

# Joystick Launcher - Architecture Documentation

## Overview

Joystick Launcher is built using modern Android development practices with Jetpack Compose, Kotlin, and follows clean architecture principles.

## Technology Stack

### Core Technologies
- **Language**: Kotlin 1.9.22
- **UI Framework**: Jetpack Compose
- **Build System**: Gradle 8.7.3
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### Key Libraries

#### UI & Compose
- `androidx.compose.ui` - Core Compose UI
- `androidx.compose.material3` - Material Design 3 components
- `androidx.compose.foundation` - Foundation components
- `androidx.activity:activity-compose` - Activity integration

#### Navigation
- `androidx.navigation:navigation-compose` - Compose Navigation

#### Image Loading
- `io.coil-kt:coil-compose` - Coil for image loading

#### Async Operations
- Kotlin Coroutines
- Flow for reactive streams

#### Android Jetpack
- `androidx.core:core-ktx` - Kotlin extensions
- `androidx.lifecycle` - Lifecycle components
- `androidx.activity` - Activity APIs

## Project Structure

```
app/src/main/java/dev/gafoor/joysticklauncher/
├── MainActivity.kt              # Main entry point
├── MainActivity2.kt             # Secondary activity
├── MainScreen.kt                # Main screen composable
├── FirstFragment.kt             # Fragment (if used)
├── NotificationData.kt          # Data model for notifications
├── NotificationListenerService.kt # Notification service
├── NotificationViewModel.kt     # ViewModel for notifications
│
├── home/
│   ├── HomeScreen.kt           # Home screen UI
│   ├── Joystick.kt             # Joystick component
│   ├── Notifications.kt         # Notification UI components
│   └── NotificationShadeService.kt # Notification shade logic
│
├── appdrawer/
│   └── AppDrawer.kt            # App drawer UI
│
├── secondscreen/
│   └── [Secondary screen components]
│
└── ui/
    └── theme/
        └── [Theme and styling]
```

## Architecture Pattern

### MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────┐
│                    View Layer                    │
│  (Composables - HomeScreen, Joystick, AppDrawer) │
└────────────────────┬────────────────────────────┘
                     │ Observes State
                     ↓
┌─────────────────────────────────────────────────┐
│                ViewModel Layer                   │
│      (NotificationViewModel, State Holders)      │
└────────────────────┬────────────────────────────┘
                     │ Manages Business Logic
                     ↓
┌─────────────────────────────────────────────────┐
│                   Model Layer                    │
│  (NotificationData, System Services, Repos)      │
└─────────────────────────────────────────────────┘
```

## Core Components

### 1. MainActivity

**Responsibility**: Application entry point and navigation setup

**Key Features**:
- Initializes NavHost for navigation
- Sets up edge-to-edge display
- Manages permission requests
- Handles system UI mode

**Navigation Routes**:
- `homeScreen` - Main launcher screen
- `secondScreen` - Secondary screen

### 2. MainScreen

**Responsibility**: Main launcher interface container

**Key Features**:
- Manages drawer visibility state
- Handles gesture detection
- Coordinates between home screen and app drawer
- Preloads app icons for performance

**Gestures Handled**:
- Vertical drag for drawer
- Tap for navigation
- Swipe for screen transitions

### 3. HomeScreen

**Responsibility**: Home screen UI with clock and notifications

**Components**:
- **Clock Widget**: Real-time clock with custom fonts
- **Date Display**: Current date
- **Notification Access**: Quick pull-down for notifications
- **Volume Control**: Visual volume indicator
- **App Shortcuts**: Quick access to common apps

**State Management**:
- Uses `remember` for local state
- `LaunchedEffect` for time updates
- `mutableStateOf` for reactive UI

### 4. Joystick

**Responsibility**: Interactive joystick navigation component

**Features**:
- **Drag Gesture Detection**: Tracks touch movements
- **Region Boundaries**: Defines app zones
- **Haptic Feedback**: Vibrates on region changes
- **Visual Feedback**: Animated app icons
- **Quick App Access**: Direct app launching

**Technical Details**:
- Uses `Animatable` for smooth animations
- `detectDragGestures` for touch handling
- `VibrationEffect` for haptic feedback
- Dynamic region calculation based on screen size

**Physics**:
```kotlin
boundaryValues = [1f, 22.5f, 45f, 67.5f, 91f]
regions = 5 (one for each app + center)
```

### 5. App Drawer

**Responsibility**: Display all installed applications

**Features**:
- **App List**: Alphabetically sorted apps
- **Icon Loading**: Efficient icon caching
- **Search** (planned): Quick app search
- **Launch**: One-tap app launching

**Implementation**:
- `LazyColumn` for efficient scrolling
- Package Manager integration
- Intent filtering for launchable apps

### 6. Notification System

**Components**:

#### NotificationListenerService
- Extends Android's NotificationListenerService
- Listens for system notifications
- Manages notification lifecycle

#### NotificationData (Data Class)
```kotlin
data class NotificationData(
    val key: String,
    val title: String,
    val content: String,
    val packageName: String,
    val postTime: Long,
    val pendingIntent: PendingIntent?,
    val clearAction: () -> Unit
)
```

#### NotificationViewModel
- Manages notification state
- Provides reactive updates
- Handles notification actions

#### Notification State
- `mutableStateListOf` for reactive list
- Automatically updates UI on changes

## State Management

### Compose State

```kotlin
// Local state
var drawerVisible by remember { mutableStateOf(false) }

// Shared state
val notificationList = mutableStateListOf<NotificationData>()

// Lifecycle-aware state
val currentTime by rememberUpdatedState(LocalDateTime.now())
```

### State Hoisting

- State is hoisted to parent composables
- Child composables receive callbacks
- Unidirectional data flow

## Navigation

### NavHost Structure

```kotlin
NavHost(navController, startDestination = "homeScreen") {
    composable("homeScreen") { 
        LauncherContent(navController) 
    }
    composable("secondScreen") { 
        SecondScreen() 
    }
}
```

### Navigation Flow

```
MainActivity → NavHost → LauncherContent → MainScreen
                              ↓
                         HomeScreen / AppDrawer
```

## Performance Optimizations

### 1. Icon Preloading

```kotlin
LaunchedEffect(Unit) {
    installedApps.forEach { appInfo ->
        val request = ImageRequest.Builder(context)
            .data(iconUri)
            .allowHardware(false)
            .build()
        ImageLoader(context).enqueue(request)
    }
}
```

### 2. Lazy Loading

- `LazyColumn` for app lists
- On-demand rendering
- Recycled views

### 3. Animation Optimization

- `Animatable` for smooth transitions
- Hardware acceleration enabled
- `graphicsLayer` for efficient transformations

### 4. Memory Management

- Weak references for bitmaps
- Proper lifecycle handling
- Coil's built-in caching

## Threading Model

### Main Thread (UI)
- All Compose UI updates
- User interaction handling
- Navigation

### Background Threads
- App icon loading
- Notification processing
- System queries

### Coroutines

```kotlin
// UI scope
rememberCoroutineScope().launch {
    // Suspending operations
}

// Lifecycle scope
LaunchedEffect(key) {
    // Lifecycle-bound operations
}
```

## System Integration

### 1. Package Manager
```kotlin
packageManager.queryIntentActivities(
    Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
    0
)
```

### 2. Notification Listener
```kotlin
class MyNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification)
    override fun onNotificationRemoved(sbn: StatusBarNotification)
}
```

### 3. Vibration Service
```kotlin
val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
vibratorManager.defaultVibrator.vibrate(effect)
```

### 4. Audio Manager
```kotlin
val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
```

## Design Patterns

### 1. Observer Pattern
- LiveData / State observation
- Notification updates
- Time updates

### 2. Repository Pattern
- Data access abstraction
- Package Manager queries
- System service access

### 3. Factory Pattern
- ViewModel creation
- Intent creation

### 4. Singleton Pattern
- Notification list
- System service instances

## Material Design 3

### Theme
- Dynamic color support
- Dark/Light theme
- Material You integration

### Components Used
- `Surface` - Container with elevation
- `Button` - Interactive buttons
- `ModalBottomSheet` - Bottom sheets
- `Scaffold` - Screen structure
- `Text` - Typography

## Build Configuration

### Build Variants
- Debug
- Release

### ProGuard Rules
- Located in `proguard-rules.pro`
- Optimizes release builds
- Obfuscates code

### Gradle Configuration
```gradle
android {
    compileSdk 34
    defaultConfig {
        minSdk 26
        targetSdk 34
    }
    buildFeatures {
        compose true
    }
}
```

## Testing Strategy

### Unit Tests
- ViewModel logic
- Data transformations
- Business logic

### UI Tests
- Composable testing
- Navigation testing
- Interaction testing

### Integration Tests
- System service integration
- Notification handling
- App launching

## Future Architecture Improvements

### Planned Enhancements
1. **Dependency Injection**: Hilt/Koin integration
2. **Repository Layer**: Proper data abstraction
3. **Use Cases**: Business logic separation
4. **Database**: Room for settings persistence
5. **Modularization**: Feature modules

### Scalability Considerations
- Modular architecture
- Feature flags
- Plugin system for customization
- Theme engine

## Security Considerations

1. **Permission Handling**: Minimal required permissions
2. **Data Privacy**: No external data transmission
3. **Local Storage**: Secure preferences storage
4. **Intent Validation**: Validate all intents

## Accessibility

- Large touch targets (48dp minimum)
- Content descriptions for screen readers
- Haptic feedback
- High contrast support
- Scalable text sizes

## Debugging Tools

- Logcat for logging
- Layout Inspector for UI debugging
- Android Studio Profiler for performance
- Compose Preview for UI preview

## Code Quality

### Standards
- Kotlin coding conventions
- Material Design guidelines
- Android best practices

### Documentation
- KDoc comments for public APIs
- Inline comments for complex logic
- Architecture documentation

## Dependencies Management

All dependencies are managed in `app/build.gradle`:
- Version catalogs (future)
- Centralized version management
- Regular security updates

---

This architecture is designed to be:
- **Maintainable**: Clear separation of concerns
- **Testable**: Modular and mockable components
- **Scalable**: Easy to add new features
- **Performant**: Optimized for smooth user experience
- **Modern**: Using latest Android technologies

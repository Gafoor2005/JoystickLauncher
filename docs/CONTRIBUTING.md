# Contributing to Joystick Launcher

Thank you for your interest in contributing to Joystick Launcher! This document provides guidelines and instructions for contributing to the project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [How to Contribute](#how-to-contribute)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Testing](#testing)
- [Documentation](#documentation)

## Code of Conduct

### Our Pledge

We are committed to providing a welcoming and inspiring community for all. We pledge to make participation in our project a harassment-free experience for everyone.

### Expected Behavior

- Be respectful and inclusive
- Welcome newcomers
- Accept constructive criticism gracefully
- Focus on what is best for the community
- Show empathy towards others

### Unacceptable Behavior

- Harassment or discriminatory language
- Trolling or insulting comments
- Public or private harassment
- Publishing others' private information
- Other conduct which could reasonably be considered inappropriate

## Getting Started

### Prerequisites

Before you begin, ensure you have:

- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK with API 34
- Git for version control
- Basic knowledge of Kotlin and Jetpack Compose

### Fork and Clone

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/yourusername/JoystickLauncher.git
   cd JoystickLauncher
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/originalowner/JoystickLauncher.git
   ```

## Development Setup

### 1. Open in Android Studio

- Launch Android Studio
- Select "Open an Existing Project"
- Navigate to the cloned directory
- Wait for Gradle sync

### 2. Configure Environment

- Ensure SDK is properly configured
- Install missing SDK components if prompted
- Set up an Android device or emulator

### 3. Build and Run

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Install on device
./gradlew installDebug
```

### 4. Project Structure

Familiarize yourself with:
- `app/src/main/java/dev/gafoor/joysticklauncher/` - Main source code
- `app/src/main/res/` - Resources
- `app/build.gradle` - App-level configuration
- `docs/` - Documentation

## How to Contribute

### Reporting Bugs

Before creating a bug report:
1. Check existing issues
2. Use the latest version
3. Verify the bug is reproducible

**Bug Report Template**:
```markdown
**Describe the bug**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce:
1. Go to '...'
2. Click on '...'
3. See error

**Expected behavior**
What you expected to happen.

**Screenshots**
If applicable, add screenshots.

**Device Information:**
- Device: [e.g. Pixel 6]
- Android Version: [e.g. Android 13]
- App Version: [e.g. 1.0.0]

**Additional context**
Any other relevant information.
```

### Suggesting Features

**Feature Request Template**:
```markdown
**Feature Description**
Clear description of the feature.

**Problem it Solves**
What problem does this address?

**Proposed Solution**
How would you implement this?

**Alternatives Considered**
Other solutions you've thought about.

**Additional Context**
Mockups, examples, or references.
```

### Code Contributions

#### Good First Issues

Look for issues tagged with:
- `good first issue` - Great for newcomers
- `help wanted` - We need help with these
- `documentation` - Documentation improvements

#### Areas for Contribution

1. **New Features**
   - Widget support
   - Custom gestures
   - Theme customization
   - Icon pack support

2. **Bug Fixes**
   - Check the issue tracker
   - Fix crashes or UI bugs
   - Performance improvements

3. **Documentation**
   - Improve README
   - Add code comments
   - Create tutorials
   - Translate docs

4. **Testing**
   - Write unit tests
   - Create UI tests
   - Test on different devices

## Coding Standards

### Kotlin Style Guide

Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// ✅ Good
class HomeScreen {
    private val spacing = 16.dp
    
    fun onAppClick(packageName: String) {
        launchApp(packageName)
    }
}

// ❌ Bad
class homeScreen {
    private val Spacing = 16.dp
    
    fun OnAppClick(PackageName: String) {
        launchApp(PackageName)
    }
}
```

### Compose Best Practices

```kotlin
// ✅ Good - Hoisted state
@Composable
fun MyScreen(
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    // ...
}

// ❌ Bad - State inside composable
@Composable
fun MyScreen() {
    val navController = rememberNavController()
    // ...
}
```

### File Organization

```kotlin
// Order: package → imports → class → methods
package dev.gafoor.joysticklauncher.home

import android.content.Context
import androidx.compose.runtime.Composable

class HomeScreen {
    // Companion objects
    companion object {
        const val TAG = "HomeScreen"
    }
    
    // Properties
    private val spacing = 16.dp
    
    // Methods
    fun initialize() { }
}
```

### Naming Conventions

- **Classes**: PascalCase (`HomeScreen`, `AppDrawer`)
- **Functions**: camelCase (`launchApp`, `onDrawerOpen`)
- **Variables**: camelCase (`installedApps`, `isVisible`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_APPS`, `DEFAULT_SPACING`)
- **Composables**: PascalCase (`HomeScreenContent`, `JoystickView`)

### Comments

```kotlin
// ✅ Good - Explain WHY
// Preload icons to prevent stuttering during scroll
preloadAppIcons(apps)

// ❌ Bad - Explain WHAT (code should be self-explanatory)
// Call preloadAppIcons with apps
preloadAppIcons(apps)
```

### Code Documentation

```kotlin
/**
 * Displays the main joystick interface for app navigation.
 *
 * @param packageManager The system package manager for app queries
 * @param quickApps List of apps to display on the joystick
 * @param modifier Optional modifier for customization
 */
@Composable
fun Joystick(
    packageManager: PackageManager,
    quickApps: List<ApplicationInfo>,
    modifier: Modifier = Modifier
) {
    // Implementation
}
```

## Commit Guidelines

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Formatting, no code change
- `refactor`: Code restructuring
- `perf`: Performance improvement
- `test`: Adding tests
- `chore`: Maintenance tasks

### Examples

```bash
# Good commits
feat(joystick): add haptic feedback on region change
fix(drawer): prevent crash when no apps installed
docs(readme): add installation instructions
refactor(home): extract clock component

# Bad commits
Update stuff
Fix bug
Changed things
```

### Commit Best Practices

- Keep commits atomic (one logical change)
- Write clear, descriptive messages
- Reference issues when applicable
- Use imperative mood ("add" not "added")

## Pull Request Process

### Before Submitting

1. **Update from upstream**:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Test your changes**:
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

3. **Build successfully**:
   ```bash
   ./gradlew build
   ```

4. **Lint your code**:
   ```bash
   ./gradlew lint
   ```

### Creating a Pull Request

1. **Push to your fork**:
   ```bash
   git push origin feature-branch
   ```

2. **Open PR on GitHub**
   - Use a descriptive title
   - Fill out the PR template
   - Reference related issues

3. **PR Template**:
   ```markdown
   ## Description
   Brief description of changes
   
   ## Type of Change
   - [ ] Bug fix
   - [ ] New feature
   - [ ] Documentation update
   - [ ] Refactoring
   
   ## Testing
   - [ ] Unit tests pass
   - [ ] UI tests pass
   - [ ] Manual testing completed
   
   ## Checklist
   - [ ] Code follows style guide
   - [ ] Documentation updated
   - [ ] No new warnings
   - [ ] Commits are clean
   
   ## Related Issues
   Closes #123
   ```

### Review Process

1. **Automated Checks**
   - Build must succeed
   - Tests must pass
   - Lint checks must pass

2. **Code Review**
   - At least one approval required
   - Address all comments
   - Request re-review after changes

3. **Merge**
   - Squash and merge for clean history
   - Delete branch after merge

## Testing

### Unit Tests

```kotlin
@Test
fun `joystick region calculation is correct`() {
    val boundaries = listOf(1f, 22.5f, 45f, 67.5f, 91f)
    val offset = 30f
    val region = boundaries.indexOfFirst { it > offset }
    assertEquals(2, region)
}
```

### Compose Tests

```kotlin
@Test
fun homeScreen_displaysCorrectly() {
    composeTestRule.setContent {
        HomeScreenContent(
            packageManager = mockPackageManager,
            installedApps = mockApps,
            navigateToDrawer = {}
        )
    }
    
    composeTestRule
        .onNodeWithText("Home")
        .assertIsDisplayed()
}
```

### Test Coverage

- Aim for >70% code coverage
- Test critical paths thoroughly
- Include edge cases

## Documentation

### Code Documentation

- Document all public APIs
- Explain complex algorithms
- Add examples where helpful

### README Updates

- Keep installation instructions current
- Update feature list
- Add troubleshooting steps

### Documentation Files

- Update `FEATURES.md` for new features
- Update `ARCHITECTURE.md` for structural changes
- Update `USER_GUIDE.md` for UI changes

## Questions?

- Open a discussion on GitHub
- Check existing documentation
- Ask in issues with `question` label

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.

## Recognition

Contributors will be recognized in:
- CONTRIBUTORS.md file
- Release notes
- Project documentation

Thank you for contributing to Joystick Launcher! 🎮

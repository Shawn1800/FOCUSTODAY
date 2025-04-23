# FOCUSTODAY

**Focus on what matters today, before the day ends.**

![FOCUSTODAY App](https://via.placeholder.com/800x400?text=FOCUSTODAY)

## Overview

FOCUSTODAY is an Android productivity application designed with one clear purpose: to help you accomplish your most important tasks before the day ends. With a real-time countdown showing exactly how many hours remain in your day, FOCUSTODAY creates a gentle sense of urgency that encourages completion of what truly matters.

## Key Features

### ⏳ Time-Remaining Display
- Real-time countdown showing hours and minutes left in your day
- Visual indicators that change as time grows shorter
- Customizable day start/end times to match your schedule

### ✅ Task Management with Deadlines
- Quick task entry with intuitive deadline assignment
- Priority-based sorting to focus on what's most important
- Set specific completion times for each task
- Visual progress tracking throughout the day

### 🔔 Smart Notifications
- Configurable alarms when task deadlines approach
- Completion celebrations when tasks are finished
- End-of-day summary of completed and pending tasks
- Optional gentle reminders based on your remaining time

## Why FOCUSTODAY?

Unlike traditional to-do applications that create never-ending task lists, FOCUSTODAY emphasizes the limited nature of time and helps you focus on what you can realistically accomplish today. By seeing the remaining hours in your day, you'll make more intentional choices about where to invest your time and energy.

## Getting Started

1. Download FOCUSTODAY from the Google Play Store
2. Set your typical day start and end times
3. Add your first task with a deadline
4. Start focusing on what matters most today

## Development Setup

```bash
# Clone the repository
git clone https://github.com/yourusername/focustoday.git

# Open the project in Android Studio

# Build the project
./gradlew build

# Install on your device or emulator
./gradlew installDebug
```

## Technology Stack

- Language: Java
- Platform: Android Native
- Minimum SDK: Android 7.0 (API level 24)
- Target SDK: Android 13 (API level 33)
- Local Storage: Room Database
- Notifications: Android NotificationManager
- Background Processing: WorkManager
- Architecture: MVVM with LiveData

## Screenshots

<div align="center">
  <img src="https://via.placeholder.com/250x500?text=Home+Screen" alt="Home Screen" width="250"/>
  <img src="https://via.placeholder.com/250x500?text=Add+Task" alt="Add Task" width="250"/>
  <img src="https://via.placeholder.com/250x500?text=Task+Complete" alt="Task Complete" width="250"/>
</div>

## Permissions

The app requires the following permissions:
- `RECEIVE_BOOT_COMPLETED` - To restore alarms after device restart
- `VIBRATE` - For notification alerts
- `SCHEDULE_EXACT_ALARM` - For precise task deadline notifications

## Contributing

We welcome contributions to FOCUSTODAY! Please see our [CONTRIBUTING.md](CONTRIBUTING.md) file for details on how to get involved.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details.

## Coming Soon

- Weekly patterns and insights about your productivity
- Widget for home screen with time remaining
- Google Calendar integration
- Focus sessions with Pomodoro technique
- Dark mode and additional themes

---

FOCUSTODAY - Because today is where your focus belongs.

<img width="800" height="622" alt="image" src="https://github.com/user-attachments/assets/e450ef76-b56d-4804-a47e-c47392f7259a" /># 📦 UniBox: Universal Smart Inbox

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-green?style=for-the-badge)

UniBox is a Universal Smart Inbox for Android designed to solve digital hoarding across fragmented apps. Instead of losing links, recipes, tweets, and screenshots across 10 different apps, share them all directly to UniBox via the native Android Share Sheet.

<img width="800" height="622" alt="image" src="https://github.com/user-attachments/assets/49e54754-4012-4a06-afb0-a504fe5ac1f5" />

## ✨ Key Features

<img width="561" height="377" alt="Screenshot 2026-07-12 042112" src="https://github.com/user-attachments/assets/3e9ea921-da0e-4846-9dac-a0d77cf652c7" />

- **Share Target Integration**: Seamlessly save links, text, and images from any app (Chrome, Twitter, Instagram, etc.) directly into UniBox via the Android OS Share Sheet.
- **Smart Auto-Categorization**: Automatically tags incoming links using smart categorization rules (e.g., YouTube -> Video, Amazon -> Shopping, Recipes -> Food).
- **ML Kit OCR for Images**: When you share a screenshot, UniBox runs Google ML Kit Vision on-device to extract all text, making the image instantly searchable.
- **Rich Link Previews**: Uses a background WorkManager to silently scrape OpenGraph tags (Title, Description, Image) from saved URLs using JSoup.
- **Lightning Fast Search**: Powered by a Room Database with an **FTS4 Inverted Index** for instant, zero-lag full-text search across thousands of items.
- **Location Reminders (Geofencing)**: Attach a location to a saved item and get a push notification when you physically walk within 200m of it.

<img width="590" height="616" alt="Screenshot 2026-07-12 042047" src="https://github.com/user-attachments/assets/f74d44d8-356e-40e3-abc4-b8d1b8ded53e" />

## 🛠️ Tech Stack & Architecture

<img width="632" height="596" alt="Screenshot 2026-07-12 042105" src="https://github.com/user-attachments/assets/9eda0a22-f3b7-4210-811f-bfa0c3b94b16" />

Built entirely with modern Android development standards and best practices:

- **UI**: Jetpack Compose (Material 3), Navigation Compose for fully declarative UI.
- **Architecture**: Clean Architecture (Presentation, Domain, Data layers), MVVM, Unidirectional Data Flow (UDF).
- **Concurrency**: Kotlin Coroutines & Flow (`StateFlow`, `SharedFlow`, `debounce`, `flatMapLatest`).
- **Dependency Injection**: Dagger Hilt for robust dependency management.
- **Persistence**: Room Database (leveraging `@Fts4` for fast search).
- **Background Work**: WorkManager for reliable metadata fetching and processing.
- **Machine Learning**: Google ML Kit (Text Recognition API v2) for on-device OCR.
- **Network / Scraping**: JSoup for robust HTML and OpenGraph parsing.
- **Image Loading**: Coil for fast, modern image caching and loading.

<img width="567" height="518" alt="Screenshot 2026-07-12 042054" src="https://github.com/user-attachments/assets/6b357888-6533-489d-853d-8b491f73ca21" />

### Architecture Overview

1. **Presentation Layer**: Contains ViewModels and Compose UI. Subscribes to Flows from the Domain layer.
2. **Domain Layer**: Contains the core business logic (Use Cases, Models, Repository Interfaces). Fully independent of Android frameworks.
3. **Data Layer**: Implements Repository interfaces. Handles Room Database interactions, background WorkManager tasks, and external parsing (JSoup).

<img width="547" height="515" alt="Screenshot 2026-07-12 042140" src="https://github.com/user-attachments/assets/ed96bf9c-1ba2-4993-9dca-1e095b48a667" />

## 🚀 Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites

- Android Studio (Jellyfish 2023.3.1 or later recommended).
- Java Development Kit (JDK) 17+.
- Minimum Android SDK 24 (Android 7.0), Target SDK 34.

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/unibox.git
   ```

2. **Open the project in Android Studio:**
   - Select `File > Open` and choose the cloned `unibox` directory.
   - Let Gradle complete the sync process.

3. **Build and Run:**
   - Select an emulator or connected physical device.
   - Click the "Run" button (`Shift + F10`).

### Testing the Share Target

To test how UniBox intercepts shared content:
1. Open the Chrome app (or YouTube, Twitter) on the emulator/device.
2. Go to any website or content.
3. Tap the "Share" button.
4. Select "Save to UniBox" from the Android Share Sheet.
5. The link will be instantly categorized and saved!

**Documentation in the form of an obsidian vault is available in the repository**

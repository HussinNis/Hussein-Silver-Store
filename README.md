# Hussein Silver Store - متجر حسين للفضيات

An Android application for a silver jewelry store, supporting product browsing, live silver price updates, a price calculator, shopping cart, and Firebase authentication.

## Features

- **Authentication**: Register and login with Firebase Auth
- **Live Silver Price**: Real-time silver price from exchange rate API
- **Bullion Products**: Silver bars and ounces (Italian, French, Swiss)
- **Accessories**: Rings, bracelets, and chains with gender filtering
- **Price Calculator**: Calculate price based on weight in grams
- **Shopping Cart**: Add/remove items with Firestore persistence
- **Full RTL Support**: Arabic interface

## Requirements

- Android Studio Hedgehog or newer
- Java 11 (JDK 11+)
- Gradle 8.5
- Android SDK 34
- Google Services / Firebase project

## Setup

1. Clone the repository
2. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
3. Enable **Authentication** (Email/Password) and **Cloud Firestore**
4. Download `google-services.json` and place it in `app/`
5. Open in Android Studio and sync Gradle
6. Run on an emulator or device (minSdk 24)

## Build Configuration

| Property           | Value         |
|--------------------|---------------|
| Gradle             | 8.5           |
| AGP                | 8.2.2         |
| compileSdk         | 34            |
| targetSdk          | 34            |
| minSdk             | 24            |
| Java               | VERSION_11    |
| Firebase BOM       | 33.0.0        |
| Material           | 1.11.0        |
| AppCompat          | 1.6.1         |
| RecyclerView       | 1.3.1         |
| Retrofit           | 2.9.0         |

## Project Structure

```
app/src/main/java/com/husseinsilver/store/
├── activities/         # All Activity classes
├── adapters/           # RecyclerView adapters
├── firebase/           # Firebase helper managers
├── models/             # Data models (User, Product, CartItem)
├── network/            # Retrofit client and API interfaces
├── utils/              # Constants and SharedPreferences helper
└── MyApplication.java  # Application class
```

## License

This project is for educational/portfolio purposes.

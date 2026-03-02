# Hussein Silver Store - متجر حسين للفضيات

An Android application for a silver jewelry store with Arabic RTL UI, supporting product browsing,
live silver price updates in ILS (Israeli Shekel), a price calculator, shopping cart, and Firebase
authentication.

## Features

- **Welcome Screen**: Single "تسجيل الدخول" button that opens a dialog to choose Login or Register
- **Authentication**: Email/Password login and registration via Firebase Auth
- **Live Silver Price in ILS**: Real-time silver ounce price converted to ILS from exchange rate API, with SharedPreferences caching as fallback
- **Bullion Products**: Silver bars (250g, 500g, 1kg) and troy ounces (Italian, French, Swiss)
- **Accessories**: Rings, bracelets, and chains with tab-based gender/type filtering
- **Price Calculator**: Calculate price for weight in grams; shows base, margin (10%), and final price in ILS
- **Shopping Cart**: Add/remove/update items; separate totals for weighted (bullion) and accessories items
- **Full RTL Support**: Arabic interface with `android:supportsRtl="true"`

## Firebase Setup Instructions

1. Go to [Firebase Console](https://console.firebase.google.com) and create a new project named **Hussein Silver Store**
2. Enable **Authentication** → Sign-in method → **Email/Password**
3. Enable **Cloud Firestore** → Start in production mode
4. Register your Android app with package name: `com.husseinsilver.store`
5. Download the generated `google-services.json` and place it in the `app/` directory (replacing the placeholder)
6. Import the Firestore security rules from `firestore.rules` (see Firestore rules section below)

### Firestore Data Structure

```
users/{uid}
  - email: String
  - fullName: String
  - createdAt: long (timestamp ms)

products/{productId}
  - name: String
  - category: "bullion" | "accessories"
  - isWeighted: Boolean
  - weightGrams: Number (for bullion)
  - origin: String (optional)
  - priceIls: Number (for accessories)
  - active: Boolean

carts/{uid}/items/{productId}
  - productId: String
  - productName: String
  - priceIls: Number
  - quantity: Number
  - weightGrams: Number
  - isWeighted: Boolean
  - category: String
```

### Firestore Security Rules

The file `firestore.rules` at the repo root contains the rules. To deploy:

```bash
firebase deploy --only firestore:rules
```

Rules summary:
- `products`: **public read**, no public write
- `users/{uid}`: only the authenticated user with matching uid can read/write
- `carts/{uid}/items/{itemId}`: only the authenticated user with matching uid can read/write

## Requirements

- Android Studio Hedgehog or newer
- Java 11+
- Gradle 8.5
- Android SDK 34
- Google Services / Firebase project (see setup above)

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
├── activities/         # WelcomeActivity, LoginActivity, RegisterActivity,
│                       # MainActivity, BullionActivity, AccessoriesActivity,
│                       # CalculatorActivity, CartActivity
├── adapters/           # ProductAdapter, CartAdapter
├── firebase/           # AuthManager, FirebaseManager, FirestoreManager
├── models/             # User, Product, CartItem
├── network/            # RetrofitClient, ApiService, SilverPriceResponse
├── utils/              # Constants, SharedPreferencesManager
└── MyApplication.java
```

## Silver Price Logic

- Fetches `XAG` (silver) and `ILS` rates from `https://api.exchangerate.host/latest?base=USD&symbols=XAG,ILS`
- `xagPriceUSD = 1 / rates["XAG"]`
- `ouncePriceILS = xagPriceUSD × rates["ILS"]`
- Cached in SharedPreferences; shown on failure with a note

### Calculator Formula

- `gramPriceILS = ouncePriceILS / 31.1034768`
- `base = grams × gramPriceILS`
- `margin = base × 0.10`
- `final = base + margin`

### Cart Total Formula

- `weightedSubtotal = Σ (qty × weightGrams × gramPriceILS)` — bullion items only
- `margin = weightedSubtotal × 0.10`
- `accessoriesSubtotal = Σ (qty × priceILS)` — accessories items only
- `total = weightedSubtotal + margin + accessoriesSubtotal`

## License

This project is for educational/portfolio purposes.


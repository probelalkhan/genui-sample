# Library Book Parser

A modern Android demo app that uses **Firebase AI Logic SDK** with **Gemini 2.0 Flash Lite** to extract structured book borrowing information from natural language input.

## Features

✨ **Modern Material 3 UI** with beautiful gradient backgrounds  
🤖 **Gemini 2.0 Flash Lite Integration** for intelligent text parsing  
🎨 **Syntax-Highlighted JSON Output** with color-coded fields  
⚡ **Real-time Processing** with loading states and error handling  
📱 **Responsive Design** that works on all Android devices  

## Prerequisites

1. **Android Studio**: Latest version with Kotlin support
2. **Firebase Project**: Set up a Firebase project with AI Logic enabled
3. **Minimum SDK**: Android 12 (API 31)
4. **Target SDK**: Android 14 (API 36)

## Setup Instructions

### 1. Firebase Configuration

**IMPORTANT**: This app requires a Firebase configuration file to function.

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Enable **Firebase AI Logic** for your project
4. Download the `google-services.json` file for your Android app
5. **Replace** the placeholder file:
   - Delete: `app/google-services.json.PLACEHOLDER`
   - Copy your actual `google-services.json` to `app/google-services.json`

⚠️ **Without the actual `google-services.json` file, the app will not build.**

### 2. Build the App

```bash
./gradlew assembleDebug
```

### 3. Install on Device/Emulator

```bash
./gradlew installDebug
```

Or use Android Studio's Run button.

## How It Works

1. **Input**: Enter a natural language request about borrowing a book
   - Example: "I want to borrow 'The Great Gatsby' by F. Scott Fitzgerald from January 15th to January 30th."

2. **Processing**: Click "Extract Information" button
   - The app sends the request to Gemini 2.0 Flash Lite via Firebase AI Logic SDK
   - The model extracts structured information

3. **Output**: View beautifully formatted JSON with:
   - `bookName`: Title of the book
   - `author`: Author name
   - `from`: Start date (YYYY-MM-DD)
   - `till`: End date (YYYY-MM-DD)

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **AI Model**: Gemini 2.0 Flash Lite (via Firebase AI Logic)
- **Architecture**: MVVM with StateFlow
- **Serialization**: kotlinx-serialization

## Project Structure

```
app/src/main/java/dev/belalkhan/genui/
├── MainActivity.kt              # Main UI with BookExtractionScreen
├── BookExtractionViewModel.kt   # Business logic and Gemini integration
└── ui/theme/                    # Material 3 theme configuration
```

## Demo Features

- ✅ Pre-filled demo text for instant testing
- ✅ Loading indicators during API calls
- ✅ Error handling with user-friendly messages
- ✅ Smooth animations and transitions
- ✅ Dark-themed JSON output for readability
- ✅ Info card with usage instructions

## License

This is a demo application created for educational purposes.

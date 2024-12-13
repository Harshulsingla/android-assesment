# Offline Dictionary App

**Objective**
The Offline Dictionary App is designed to fetch word definitions from the Dictionary API, provide search functionality, display word details (such as synonyms, antonyms, and pronunciation), and offer offline access via a Room database.

**Features**
1. Word Search Screen
Search Functionality
Search for words using a SearchView or search bar.
Fetch and display word details including definitions, pronunciation, part of speech, and examples via the Dictionary API.
UI Layout
Display:
Word Title
Phonetic Pronunciation with Audio Playback (MediaPlayer).
List of Meanings grouped by Part of Speech.
Definitions with examples (if available).
Save Offline
Button to save word details locally in the Room database for offline access.
2. Word Details Screen
Navigation
Clicking on a saved word or search result navigates to the Word Details Screen.
Word Information
Display Word Title, Phonetic Pronunciation with playback, List of meanings, Synonyms, and Antonyms.
Offline Management
Allow users to delete words from offline storage.
3. Saved Words List (Offline Support)
Saved Words Screen
Display saved words from the Room database.
Features
Empty State: Message displayed when no saved words are available.

**Advanced Features**
Audio Playback
Play the sound of the word using the provided URL in the API response.

**API Information**
Endpoint: https://api.dictionaryapi.dev/api/v2/entries/en/{word}
Fetch word details including definitions, synonyms, antonyms, pronunciation, part of speech, and examples.

**Installation**
Clone the repository:
Open the project in Android Studio.
Sync Gradle and resolve any dependencies.
Run the app on an emulator or a physical device.

**Libraries Used**
Retrofit: For API calls.
Room: Local database for offline access.
MediaPlayer: For playing audio pronunciations.
SharedPreferences: For saving search history and theme preferences.
Material Components: For UI components and Dark Mode support.

**Key Features Implemented**
Word Search Screen with API integration and offline saving.
Word Details Screen with pronunciation playback and offline delete functionality.
Saved Words List.

**Screenshots**
Include the following screenshots:
Word Search Screen
Word Details Screen
Saved Words Screen

**Advanced Features**
Filters for saved words by part of speech.
Audio playback functionality for word pronunciation.
Synchronization
Data is synchronized periodically to update offline definitions.

**Notes**
Ensure the API endpoint is accessible for testing.
Handle any API rate limits or restrictions.

**How to Contribute**
Fork the repository.
Create a feature branch:
Commit changes and push to your fork.
Submit a pull request.

**License**
This project is licensed under the MIT License. See the LICENSE file for more information.
git checkout -b feature-name
git clone <repository-url>

# Sparvel

**Technology stack**:
* Jetpack Compose
* Navigation Compose
* Material Design 3
* Room
* MediaPlayer
* TalkBack
* Firebase Analytics (maybe?)

**Package organization**:
* album - everything related to album (Albums Screen, Album Screen)
* playlist - everything related to playlists (list of playlists, playlist info, editing and adding new playlist)
* track - everything related to track item (editing track info, adding new tracks to playlist)
* player - draggable music player here
* equalizer - equalizer screen
* main - main screen with left drawer

**TODO list**:
* add playlist creation/removal etc screens + local database for storing them
* run ui through set of layout inspector test: check performance and recomposition. especially for draggable view
* update toolbar icons color in player depending on the background (if it is too bright, change color of icons to black)
* fix touch handler of player view
* extract build version into `BuildConfig`

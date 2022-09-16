# Sparvel

Technology stack:
* Jetpack Compose
* Navigation Compose
* Material Design 3
* Room
* MediaPlayer
* TalkBack
* Firebase Analytics (maybe?)

Package organization:
* album - everything related to album (Albums Screen, Album Screen)
* playlist - everything related to playlists (list of playlists, playlist info, editing and adding new playlist)
* track - everything related to track item (editing track info, adding new tracks to playlist)
* player - draggable music player here
* equalizer - equalizer screen
* main - main screen with left drawer

TODO list:
* in night mode collection items' image fadeout effect is screwed due to background gradient
* fix double @Inject call in viewmodels (baseViewModel accepts injection as class field, not in constructor)
* add animation for switching screens in HomeScreenContainer
* fix no-image gradient in albums/playlists screens
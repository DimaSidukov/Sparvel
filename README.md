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
* Instant images uploading for track cover arts on Home Screen (as alternative, load images as compressed bitmaps while splash screen is visible)
* for each cover art run its own coroutine that adds image to hashmap by idx and start home screen when it's ready. if images are not loaded yet add 2 seconds delay, do it more times in case of faliure, eventually on reaching 10 seconds delay start home screen with what is loaded, whether there are images or just null
* to implement the approach above I can load data on splash screen and pass it as arguments to the home screen
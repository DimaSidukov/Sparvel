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
* set color ripple effect for night theme
* some symbols are not well visible in search bar, such as 'g'
* while searching for music images are not being loaded - this disappears when result list is scrollable, but noticeable on small result lists
* refer to this: https://developer.android.com/jetpack/compose/side-effects#remembercoroutinescope It might be better to use this approach in SplashScreen. As data fetched and animation is finished, wait some time, such as 300ms and navigate to the next screen

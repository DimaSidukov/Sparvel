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
* update toolbar icons color in player depending on the background (if it is too bright, change color of icons to black)
* extract build version into `BuildConfig`
* put searchbar and drawer into toolbar
* use `.background()` for gradient instead of `graphicsLayer + drawWithContent`. It also accepts brush as parameter
* flat boxes in playerview and use verticalArrangement/gravity or smth for actual arrangement of elements inside
* add blur to the background of the player
* fix no-image state of player for collapsing and expanding view - brings a lot of screen freeze right now
* add logo to the app
* recomendation: when implementing playlists, it's better store only track ids in database and then when user opens the screen, load data by these ids. No sense in storing track data, since user may not open some playlist for undefined period of time

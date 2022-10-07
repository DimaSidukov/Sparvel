package com.sidukov.sparvel.core.functionality

import androidx.navigation.NavController

sealed class Screens(val route: String) {

    companion object {
        private const val splashTitle = "splash"
        private const val drawerContainerTitle = "drawer_container"
        private const val homeTitle = "home"
        private const val playlistsTitle = "playlists"
        private const val playlistTitle = "playlist"
        private const val newPlaylistTitle = "new_playlist"
        private const val albumsTitle = "albums"
        private const val albumTitle = "album"
        private const val libraryTitle = "library"
        private const val editTrackInfoTitle = "edit_track_info"
        private const val addTracksTitle = "add_tracks"
        private const val equalizerTitle = "equalizer"
    }

    object Splash : Screens(splashTitle)
    object DrawerContainer : Screens(drawerContainerTitle)
    object Home : Screens("$homeTitle?tracks={tracks}") {
        fun passTrackList(tracks: String) = "$homeTitle?tracks=$tracks"
    }
    object Playlists : Screens(playlistsTitle)
    object Playlist : Screens(playlistTitle)
    object NewPlaylist : Screens(newPlaylistTitle)
    object Albums : Screens(albumsTitle)
    object Album : Screens(albumTitle)
    object Library : Screens(libraryTitle)
    object EditTrackInfo : Screens(editTrackInfoTitle)
    object AddTracks : Screens(addTracksTitle)
    object Equalizer : Screens(equalizerTitle)
}

fun NavController.navigateAndSetRoot(target: String) {
    navigate(target) {
        this@navigateAndSetRoot.currentBackStackEntry?.destination?.route.let {
            it?.let { route ->
                popUpTo(route) {
                    inclusive = true
                }
            }
        }
    }
}
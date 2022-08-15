package com.sidukov.sparvel.core.functionality

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sidukov.sparvel.features.album.AlbumScreen
import com.sidukov.sparvel.features.album.AlbumsScreen
import com.sidukov.sparvel.features.equalizer.EqualizerScreen
import com.sidukov.sparvel.features.home.HomeScreen
import com.sidukov.sparvel.features.library.LibraryScreen
import com.sidukov.sparvel.features.playlist.NewPlaylistScreen
import com.sidukov.sparvel.features.playlist.PlaylistScreen
import com.sidukov.sparvel.features.playlist.PlaylistsScreen
import com.sidukov.sparvel.features.track.AddTracksScreen
import com.sidukov.sparvel.features.track.EditTrackInfoScreen

object Route {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val PLAYLISTS = "playlists"
    const val PLAYLIST = "playlist"
    const val NEW_PLAYLIST = "new_playlist"
    const val ALBUMS = "albums"
    const val ALBUM = "album"
    const val LIBRARY = "library"
    const val TRACK_INFO = "track_info"
    const val ADD_TRACKS = "add_tracks"
    const val EQUALIZER = "equalizer"
}

@Composable
fun ScreenNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = Route.HOME) {
        composable(Route.HOME) {
            HomeScreen()
        }
        composable(Route.ALBUMS) {
            AlbumsScreen()
        }
        composable(Route.ALBUM) {
            AlbumScreen()
        }
        composable(Route.PLAYLISTS) {
            PlaylistsScreen()
        }
        composable(Route.PLAYLIST) {
            PlaylistScreen()
        }
        composable(Route.NEW_PLAYLIST) {
            NewPlaylistScreen()
        }
        composable(Route.LIBRARY) {
            LibraryScreen()
        }
        composable(Route.TRACK_INFO) {
            EditTrackInfoScreen()
        }
        composable(Route.ADD_TRACKS) {
            AddTracksScreen()
        }
        composable(Route.EQUALIZER) {
            EqualizerScreen()
        }
    }
}
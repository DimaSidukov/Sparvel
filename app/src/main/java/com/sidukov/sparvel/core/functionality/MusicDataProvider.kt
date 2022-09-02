package com.sidukov.sparvel.core.functionality

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MusicDataProvider @Inject constructor(private val context: Context) {

    companion object {
        private const val UNNAMED_TRACK = "No name"
        private const val UNNAMED_ARTIST = "Unknown artist"
        private const val UNNAMED_ALBUM = "Unknown album"
        private const val ALBUM_URI = "content://media/external/audio/albumart"
    }

    private val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.ALBUM_ID
        ),
        MediaStore.Audio.Media.IS_MUSIC,
        null,
        null
    )

    suspend fun getAllDeviceTracks(): Flow<Pair<List<Track>, List<MusicCollection>>> = flow {
        mutableListOf<Track>().apply {
            cursor?.let { c ->
                while (c.moveToNext()) {
                    val track = Track(
                        id = c.getString(0),
                        track = c.getString(1) ?: UNNAMED_TRACK,
                        title = c.getString(2),
                        composer = c.getString(3) ?: UNNAMED_ARTIST,
                        album = c.getString(5) ?: UNNAMED_ALBUM,
                        cover = context.getBitmapOrNull(
                            Uri.parse(ALBUM_URI),
                            c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                        ),
                        duration = c.getString(4).toLong(),
                        year = (c.getString(6) ?: "-1").toInt(),
                    )
                    this.add(track)
                    emit(
                        Pair(this@apply, this@apply.toMusicCollection())
                    )
                }
            }
        }
        cursor?.close()
    }
}
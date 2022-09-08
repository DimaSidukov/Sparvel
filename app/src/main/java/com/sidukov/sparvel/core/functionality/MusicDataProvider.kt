package com.sidukov.sparvel.core.functionality

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import javax.inject.Inject


class MusicDataProvider @Inject constructor(private val context: Context) {

    companion object {
        private const val UNNAMED_TRACK = "No name"
        private const val UNNAMED_ARTIST = "Unknown artist"
        private const val UNNAMED_ALBUM = "Unknown album"
        private val ALBUM_URI = Uri.parse("content://media/external/audio/albumart")
    }

    fun getAllDeviceTracks(): Pair<List<Track>, List<MusicCollection>> {
        val c = context.contentResolver.query(
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
        val list = mutableListOf<Track>()
        try {
            while (c?.moveToNext() == true) {
                list.add(
                    Track(
                        id = c.getString(0),
                        track = c.getString(1) ?: UNNAMED_TRACK,
                        title = c.getString(2),
                        composer = c.getString(3) ?: UNNAMED_ARTIST,
                        album = c.getString(5) ?: UNNAMED_ALBUM,
                        coverId = ContentUris.withAppendedId(
                            ALBUM_URI,
                            c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                        ).toString(),
                        duration = c.getString(4).toLong(),
                        year = (c.getString(6) ?: "-1").toInt(),
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("CURSOR ERROR", e.message.toString())
        } finally {
            c?.close()
        }
        return Pair(list, list.toMusicCollection())
    }
}
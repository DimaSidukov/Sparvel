package com.sidukov.sparvel.core.functionality.service

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.sidukov.sparvel.core.model.Track
import javax.inject.Inject
import kotlin.math.ceil

class MusicDataProvider @Inject constructor(private val context: Context) {

    companion object {
        private const val UNKNOWN_PATTERN = "unknown"
        private const val UNNAMED_TRACK = "No name"
        private const val UNNAMED_ARTIST = "Unknown artist"
        private const val UNNAMED_ALBUM = "Unknown album"
        private val ALBUM_URI = Uri.parse("content://media/external/audio/albumart")
    }

    fun getAllDeviceTracks(): List<Track> {
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
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATE_ADDED
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
                        track = c.getString(1).formatUnknown(UNNAMED_TRACK),
                        name = c.getString(2),
                        artist = c.getString(3).formatUnknown(UNNAMED_ARTIST),
                        album = c.getString(5).formatUnknown(UNNAMED_ALBUM),
                        coverId = ContentUris.withAppendedId(
                            ALBUM_URI,
                            c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                        ).toString(),
                        duration = ceil(c.getString(4).toDouble() / 1000).toInt(),
                        year = (c.getString(6) ?: "-1").toInt(),
                        dateAdded = c.getInt(8)
                    )
                )
            }
        } catch (e: Exception) {
            // either delete or make sense out of it (sending error to the server, for example)
            Log.e("CURSOR ERROR", e.message.toString())
        } finally {
            c?.close()
        }
        return list.sortedByDescending { it.dateAdded }
    }

    private fun String?.formatUnknown(target: String) =
        if (this == null || this.contains(UNKNOWN_PATTERN)) target else this
}
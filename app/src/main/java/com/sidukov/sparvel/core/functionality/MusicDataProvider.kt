package com.sidukov.sparvel.core.functionality

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.sidukov.sparvel.core.model.TrackItem
import javax.inject.Inject

class MusicDataProvider @Inject constructor(context: Context) {

    private val artistId = MediaStore.Audio.Media.ARTIST_ID
    private val dateAdded = MediaStore.Audio.Media.DATE_ADDED

    private val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.DATA
        ),
        MediaStore.Audio.Media.IS_MUSIC,
        null,
        null
    )

    fun getAllDeviceTracks(): List<TrackItem> {

        return mutableListOf<TrackItem>().apply {
            cursor?.let { c ->
                val mediaDataRetriever = MediaMetadataRetriever()
                while (c.moveToNext()) {
                    this.add(
                        TrackItem(
                            id = c.getString(0),
                            track = c.getString(1) ?: "undefined",
                            title = c.getString(2),
                            composer = c.getString(3) ?: "unknown",
                            album = c.getString(5) ?: "unknown",
                            cover = mediaDataRetriever.let {
                                it.setDataSource(c.getString(8))
                                val data = it.embeddedPicture
                                data?.let { byteArray ->
                                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                }
                            },
                            duration = c.getString(4).toLong(),
                            year = (c.getString(7) ?: "-1").toInt()

                        )
                    )
                }
                mediaDataRetriever.release()
            }
        }
    }
}
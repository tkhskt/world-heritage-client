package com.github.gericass.world_heritage_client.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.gericass.world_heritage_client.data.model.*

@Database(
    entities = [
        Keyword::class,
        ViewingHistory::class,
        VideoEntity::class,
        VideoPlayList::class,
        PlayList::class
    ], version = 1
)
@TypeConverters(DateConverter::class)
abstract class AvgleDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
    abstract fun viewingHistoryDao(): ViewingHistoryDao
    abstract fun playListDao(): PlayListDao
    abstract fun videoDao(): VideoDao
}

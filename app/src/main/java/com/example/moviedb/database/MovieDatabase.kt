package com.example.moviedb.database

import android.content.Context
import androidx.media3.common.FileTypes
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.moviedb.models.Movie
import com.example.moviedb.utils.MovieCacheType

class Converters {
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }

    @TypeConverter
    fun enumToInt(type: MovieCacheType): Int {
        return type.ordinal
    }

    @TypeConverter
    fun intToEnum(int: Int): MovieCacheType {
        return if(int == 0) MovieCacheType.REGULAR else MovieCacheType.FAVORITE
    }
}

@Database(entities = [Movie::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDataAccessObject

    companion object {
        @Volatile
        private var Instance: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MovieDatabase::class.java, "movie_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
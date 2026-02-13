package dev.terry1921.nenektrivia.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Type converters for Room.
 */
class RoomConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? = value?.let(gson::toJson)

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}

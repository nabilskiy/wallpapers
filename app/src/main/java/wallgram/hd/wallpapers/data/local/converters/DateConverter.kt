package wallgram.hd.wallpapers.data.local.converters

import androidx.room.TypeConverter
import org.joda.time.DateTime

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): DateTime? {
        return if (dateLong == null) null else DateTime(dateLong)
    }

    @TypeConverter
    fun fromDate(date: DateTime?): Long? {
        return date?.millis
    }
}
package ru.sis.statube.db.mapper

import io.realm.RealmList
import org.joda.time.DateTime
import ru.sis.statube.db.entity.DataDailyEntity
import ru.sis.statube.model.DataDaily

class DataDailyEntityMapper {

    fun map(from: DataDailyEntity): DataDaily? {
        val dataDaily = DataDaily()
        dataDaily.date = DateTime(from.date)
        dataDaily.subs = from.subs ?: return null
        dataDaily.views = from.views ?: return null
        return dataDaily
    }

    fun map(from: List<DataDailyEntity>): List<DataDaily> {
        val dataDailyList = ArrayList<DataDaily>()
        from.forEach { entity ->
            map(entity)?.let { dataDaily ->
                dataDailyList.add(dataDaily)
            }
        }
        return dataDailyList
    }

    fun reverseMap(from: DataDaily): DataDailyEntity {
        val dataDaily = DataDailyEntity()
        dataDaily.date = from.date.millis
        dataDaily.subs = from.subs
        dataDaily.views = from.views
        return dataDaily
    }

    fun reverseMap(from: List<DataDaily>): RealmList<DataDailyEntity> {
        val dataDailyList = RealmList<DataDailyEntity>()
        from.forEach { dataDaily ->
            reverseMap(dataDaily).let { entity ->
                dataDailyList.add(entity)
            }
        }
        return dataDailyList
    }

}
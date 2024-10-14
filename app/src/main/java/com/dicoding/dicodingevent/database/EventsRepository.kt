    package com.dicoding.dicodingevent.database

    import com.dicoding.dicodingevent.database.local.entity.DetailEventEntity
    import com.dicoding.dicodingevent.database.local.entity.FavoriteEventEntity
    import com.dicoding.dicodingevent.database.local.entity.FinishedEventEntity
    import com.dicoding.dicodingevent.database.local.entity.ReminderEventEntity
    import com.dicoding.dicodingevent.database.local.entity.UpcomingEventEntity
    import com.dicoding.dicodingevent.database.local.room.DetailEventDao
    import com.dicoding.dicodingevent.database.local.room.FavoriteEventDao
    import com.dicoding.dicodingevent.database.local.room.FinishedEventDao
    import com.dicoding.dicodingevent.database.local.room.ReminderEventDao
    import com.dicoding.dicodingevent.database.local.room.UpcomingEventDao
    import com.dicoding.dicodingevent.database.remote.retrofit.ApiService

    class EventsRepository private constructor(
        private val apiService: ApiService,
        private val upcomingEventDao: UpcomingEventDao,
        private val finishedEventDao: FinishedEventDao,
        private val favoriteEventDao: FavoriteEventDao,
        private val reminderEventDao: ReminderEventDao,
        private val detailDao: DetailEventDao
    ) {
        suspend fun getUpcomingEvents() = apiService.getEvents(UPCOMING_EVENT, MAX_LIMIT)

        suspend fun saveUpcomingEvent(events: List<UpcomingEventEntity>) = upcomingEventDao.insertEvents(events)

        fun getAllUpcoming() = upcomingEventDao.getActiveEvents()

        fun getHomeUpcoming() = upcomingEventDao.getHomeUpcomingEvents()

        suspend fun getFinishedEvents() = apiService.getEvents(FINISHED_EVENT, MAX_LIMIT)

        suspend fun saveFinishedEvents(events: List<FinishedEventEntity>) = finishedEventDao.insertEvents(events)

        fun getAllFinished() = finishedEventDao.getFinishedEvents()

        fun getHomeFinished() = finishedEventDao.getHomeFinished()

        fun getFavoriteEvents() = favoriteEventDao.getFavorite()

        suspend fun setFavoriteEvent(favorite: FavoriteEventEntity) = favoriteEventDao.insertFavorite(favorite)

        suspend fun deleteFavoriteEvent(id: Int) = favoriteEventDao.deleteFavorite(id)

        suspend fun getReminderEvent() = apiService.getEvents(ALL_EVENT, UPCOMING_EVENT)

        suspend fun getReminderEventSync() = reminderEventDao.getReminderEvent()

        suspend fun insertReminderEvent(event: ReminderEventEntity) = reminderEventDao.insertEvents(event)

        suspend fun getDetailEvent(id: String) = apiService.getDetailEvent(id)

        suspend fun insertDetailEvent(detail : DetailEventEntity) = detailDao.insertDetail(detail)

        fun setDetailEvent(id: String) = detailDao.getDetailEvent(id)

        suspend fun isFavorite(id: String) = detailDao.isFavorite(id)

        suspend fun updateDetail(detail: DetailEventEntity) = detailDao.updateDetail(detail)

        companion object{
            private const val UPCOMING_EVENT = 1
            private const val FINISHED_EVENT = 0
            private const val ALL_EVENT = -1
            private const val MAX_LIMIT = 40

            @Volatile
            private var instance: EventsRepository? = null
            fun getInstance(
                apiService: ApiService,
                upcomingEventDao: UpcomingEventDao,
                finishedEventDao: FinishedEventDao,
                favoriteEventDao: FavoriteEventDao,
                reminderEventDao: ReminderEventDao,
                detailDao: DetailEventDao
            ) : EventsRepository =
                instance ?: synchronized(this){
                    instance ?: EventsRepository(
                        apiService,
                        upcomingEventDao,
                        finishedEventDao,
                        favoriteEventDao,
                        reminderEventDao,
                        detailDao
                    )
                }.also { instance = it }
        }
    }
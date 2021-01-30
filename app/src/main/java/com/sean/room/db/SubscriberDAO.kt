package com.sean.room.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber): Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber) : Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber) : Int

    @Query("DELETE FROM subscriber")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM subscriber")
    fun getAllSubscribers():LiveData<List<Subscriber>>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertSubscriber2(subscriber: Subscriber): Long
//
//    @Insert
//    fun insertSubscribers(subscribers: List<Subscriber>): List<Long>
}
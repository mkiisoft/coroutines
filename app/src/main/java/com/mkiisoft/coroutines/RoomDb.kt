package com.mkiisoft.coroutines

import androidx.room.*

@Dao
interface RoomDb {

    @Insert
    suspend fun addAlbum(album: Album)

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Query("SELECT * FROM album")
    suspend fun getAlbums(): List<Album>
}


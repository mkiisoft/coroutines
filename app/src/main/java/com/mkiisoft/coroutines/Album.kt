package com.mkiisoft.coroutines

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class Album(@PrimaryKey val id: Int = 0, val album: String = "", val title: String = "", val artist: String = "")
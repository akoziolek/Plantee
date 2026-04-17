package com.example.plantee.data.repositories

import com.example.plantee.data.local.dao.MediaDao
import com.example.plantee.domain.repositories.IMediaRepository

class MediaRepository(
    private val mediaDao: MediaDao
) : IMediaRepository {

}

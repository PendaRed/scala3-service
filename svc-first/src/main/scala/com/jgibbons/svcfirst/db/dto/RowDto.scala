package com.jgibbons.svcfirst.db.dto

import java.sql.Timestamp
import java.time.LocalDateTime

case class RowDto( id: Long,
                   json: String,
                   status: String,
                   version: Int = 0,
                   createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now),
                   createdBy: String,
                   updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now),
                   updatedBy: String
                 )
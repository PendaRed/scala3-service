package com.jgibbons.svcfirst.clientdto

import com.jgibbons.svcfirst.db.dto.RowDto

import java.sql.Timestamp
import java.time.LocalDateTime

// Never expose your db dtos to the rest api.  Map them
case class ClientRowDto ( id: Long,
                     json: String,
                     status: String,
                     updatedAt: Long,
                     updatedBy: String
                   )

object ClientRowDto {
  extension (r:RowDto)
    def asClientDto = ClientRowDto(id = r.id, json = r.json, status=r.status, updatedAt = r.updatedAt.getTime, updatedBy = r.updatedBy)
}
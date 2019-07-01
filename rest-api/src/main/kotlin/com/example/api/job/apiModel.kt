package com.example.api.job

/*
import com.example.api.job.domain.db.User
import com.example.api.job.domain.db.Job
import com.example.api.job.domain.db.JobStatus
import com.example.api.job.domain.repo.JobRecordJoinUserRecord
*/
import com.example.api.job.domain.repo.JobRecordJoinUserRecord
import com.example.db.gen.tables.records.UserRecord
import com.example.db.gen.tables.records.JobRecord
import com.example.util.sql.toSqlTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.util.*

enum class JobStatus { NEW, PUBLISHED; }
data class UserCreateRequest(val name: String)
data class UserUpdateRequest(val name: String)
data class JobCreateRequest(val userId: UUID, val title: String, val status: JobStatus, val price: BigDecimal)
data class JobUpdateRequest(val title: String, val status: JobStatus, val price: BigDecimal)

data class UserDto(val id: UUID, val createdAt: Instant, val modifiedAt: Instant, val name: String)
data class JobDto(
        val id: UUID,
        val createdAt: Instant,
        val modifiedAt: Instant,
        val title: String,
        val status: JobStatus,
        val price: BigDecimal
)

data class JobWithUserDto(
        val id: UUID,
        val createdAt: Instant,
        val modifiedAt: Instant,
        val title: String,
        val status: JobStatus,
        val price: BigDecimal,
        val user: UserDto
)


fun UserRecord.toUserDto() = UserDto(
        id = id, createdAt = createdAt.toInstant(), modifiedAt = updatedAt.toInstant(), name = name
)


fun UserCreateRequest.toUserRecord(): UserRecord {
    val now = Instant.now()
    return UserRecord(
            UUID.randomUUID(),
            0,
            now.toSqlTimestamp(),
            now.toSqlTimestamp(),
            name
    )
}

fun JobCreateRequest.toJobRecord(): JobRecord {
    val now = Instant.now()
    return JobRecord(
            UUID.randomUUID(),
            userId,
            0,
            now.toSqlTimestamp(),
            now.toSqlTimestamp(),
            title,
            status.name,
            price
    )
}

fun JobRecord.toJobDto() =
        JobDto(
                id = id,
                createdAt = createdAt.toInstant(),
                modifiedAt = updatedAt.toInstant(),
                title = title,
                status = JobStatus.valueOf(status),
                price = price
        )

fun JobRecordJoinUserRecord.toJobDto() =
        JobWithUserDto(
                id = jobRecord.id,
                createdAt = jobRecord.createdAt.toInstant(),
                modifiedAt = jobRecord.updatedAt.toInstant(),
                title = jobRecord.title,
                status = JobStatus.valueOf(jobRecord.status),
                price = jobRecord.price,
                user = userRecord.toUserDto()
        )

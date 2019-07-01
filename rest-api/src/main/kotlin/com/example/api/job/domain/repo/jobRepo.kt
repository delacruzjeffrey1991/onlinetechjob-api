package com.example.api.job.domain.repo

import com.example.api.common.EntityNotFoundException
import com.example.db.gen.tables.User
import com.example.db.gen.tables.Job
import com.example.db.gen.tables.records.UserRecord
import com.example.db.gen.tables.records.JobRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
class JobRepo(private val dsl: DSLContext) {
    fun getAllRecords(): List<JobRecord> = dsl.select()
            .from(BOOK)
            .fetch()
            .into(JobRecord::class.java)

    // see: https://stackoverflow.com/questions/45342644/jooq-throws-npe-when-fetchone-is-used
    fun getOneById(id: UUID): JobRecord? = dsl.select()
            .from(BOOK)
            .where(BOOK.ID.eq(id))
            .limit(1)
            .fetch()
            .into(JobRecord::class.java)
            .firstOrNull()

    fun requireOneById(id: UUID): JobRecord = getOneById(id)
            ?: throw EntityNotFoundException("JobRecord NOT FOUND ! (id=$id)")

    fun insert(jobRecord: JobRecord) =
            dsl.insertInto(BOOK)
                    .set(jobRecord)
                    .execute()
                    .let { requireOneById(jobRecord.id) }

    fun update(jobRecord: JobRecord) =
            dsl.update(BOOK)
                    .set(jobRecord)
                    .execute()
                    .let { requireOneById(jobRecord.id) }

    fun findAllJobsJoinUser(): List<JobRecordJoinUserRecord> = dsl.select(BOOK.fields().toList())
            .select(AUTHOR.fields().toList())
            .from(AUTHOR)
            .innerJoin(BOOK)
            .on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
            .fetchGroups({ it.into(BOOK) }, { it.into(AUTHOR) })
            .map {
                val userRecord = it.value.firstOrNull()
                when (userRecord) {
                    null -> null
                    else -> JobRecordJoinUserRecord(jobRecord = it.key, userRecord = userRecord)
                }
            }.filterNotNull()

    fun findAllJobsJoinUserAsSummary(): List<JobWithUserSummary> = dsl.select(BOOK.fields().toList()).select(AUTHOR.fields().toList())
            .from(AUTHOR)
            .innerJoin(BOOK)
            .on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
            .fetchGroups({ it.into(BOOK) }, { it.into(AUTHOR) })
            .map {
                val userRecord = it.value.firstOrNull()
                val jobRecord = it.key
                when (userRecord) {
                    null -> null
                    else -> JobWithUserSummary(
                            userId = jobRecord.userId,
                            userName = userRecord.name,
                            jobId = jobRecord.id,
                            jobTitle = jobRecord.title
                    )
                }
            }.filterNotNull()


    companion object {
        private val BOOK = Job.BOOK
        private val AUTHOR = User.AUTHOR
    }
}

data class JobWithUserSummary(val userId: UUID, val userName: String, val jobId: UUID, val jobTitle: String)
data class JobRecordJoinUserRecord(val jobRecord: JobRecord, val userRecord: UserRecord)
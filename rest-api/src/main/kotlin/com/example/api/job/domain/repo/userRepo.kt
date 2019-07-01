package com.example.api.job.domain.repo

import com.example.api.common.EntityNotFoundException
import com.example.db.gen.tables.User
import com.example.db.gen.tables.records.UserRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
class UserRepo(private val dsl: DSLContext) {
    fun getAllRecords(): List<UserRecord> = dsl.select()
            .from(AUTHOR)
            .fetch()
            .into(UserRecord::class.java)

    // see: https://stackoverflow.com/questions/45342644/jooq-throws-npe-when-fetchone-is-used
    fun getOneById(id: UUID): UserRecord? = dsl.select()
            .from(AUTHOR)
            .where(AUTHOR.ID.eq(id))
            .limit(1)
            .fetch()
            .into(UserRecord::class.java)
            .firstOrNull()

    fun requireOneById(id: UUID): UserRecord = getOneById(id)
            ?: throw EntityNotFoundException("UserRecord NOT FOUND ! (id=$id)")

    fun insert(userRecord: UserRecord) =
            dsl.insertInto(AUTHOR)
                    .set(userRecord)
                    .execute()
                    .let { requireOneById(userRecord.id) }

    fun update(userRecord: UserRecord) =
            dsl.update(AUTHOR)
                    .set(userRecord)
                    .execute()
                    .let { requireOneById(userRecord.id) }


    companion object {
        private val AUTHOR = User.AUTHOR
    }
}
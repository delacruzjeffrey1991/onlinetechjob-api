package com.example.api.job


import com.example.api.job.domain.repo.UserRepo
import com.example.api.job.domain.repo.JobRepo
import com.example.util.sql.toSqlTimestamp
import mu.KLogging
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
class JobApiController(
        private val userRepo: UserRepo,
        private val jobRepo: JobRepo
) {

    @GetMapping("/api/job/users")
    fun usersFindAll() =
            userRepo.getAllRecords()
                    .toList()
                    .map { it.toUserDto() }

    @GetMapping("/api/job/users/{id}")
    fun usersGetOne(@PathVariable id: UUID) =
            userRepo.requireOneById(id)
                    .toUserDto()

    @PutMapping("/api/job/users")
    fun usersCreateOne(@RequestBody req: UserCreateRequest) =
            req.toUserRecord()
                    .let { userRepo.insert(it) }
                    .also { logger.info { "Updated Record: $it" } }
                    .toUserDto()

    @PostMapping("/api/job/users/{id}")
    fun usersUpdateOne(@PathVariable id: UUID, @RequestBody req: UserUpdateRequest): UserDto = userRepo.requireOneById(id)
            .apply {
                updatedAt = Instant.now().toSqlTimestamp()
                name = req.name
            }
            .let { userRepo.update(it) }
            .also { logger.info { "Updated Record: $it" } }
            .toUserDto()

    @GetMapping("/api/job/jobs/{id}")
    fun jobsGetOne(@PathVariable id: UUID) =
            jobRepo.requireOneById(id).toJobDto()

    @PutMapping("/api/job/jobs")
    fun jobsCreateOne(@RequestBody req: JobCreateRequest) =
            req.toJobRecord()
                    .let { jobRepo.insert(it) }
                    .also { logger.info { "Updated Record: $it" } }
                    .toJobDto()

    @PostMapping("/api/job/jobs/{id}")
    fun jobsUpdateOne(@PathVariable id: UUID, @RequestBody req: JobUpdateRequest) = jobRepo.requireOneById(id)
            .apply {
                updatedAt = Instant.now().toSqlTimestamp()
                title = req.title
                status = req.status.name
                price = req.price
            }
            .let { jobRepo.update(it) }
            .also { logger.info { "Updated Record: $it" } }
            .toJobDto()

    @GetMapping("/api/job/jobs")
    fun jobsFindAll() = jobRepo.findAllJobsJoinUser()
            .map { it.toJobDto() }
            .also { logger.info { it } }

    @GetMapping("/api/job/jobs/summary")
    fun jobsFindAllAsSummary() = jobRepo.findAllJobsJoinUserAsSummary()

    companion object : KLogging()
}
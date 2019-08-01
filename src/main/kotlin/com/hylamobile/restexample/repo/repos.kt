package com.hylamobile.restexample.repo

import com.hylamobile.restexample.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface UserRepository : JpaRepository<User, Long> {
    fun findByName(name: String?): User?
}

@RepositoryRestResource
interface PollRepository : JpaRepository<Poll, Long>

@RepositoryRestResource(exported = false)
interface OptionRepository : JpaRepository<Option, Long>

@RepositoryRestResource(excerptProjection = DetailedVote::class)
interface VoteRepository : JpaRepository<Vote, Long>

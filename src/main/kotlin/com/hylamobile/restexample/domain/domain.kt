package com.hylamobile.restexample.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.AbstractPersistable
import org.springframework.data.rest.core.config.Projection
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

open class Persistable : AbstractPersistable<Long>() {

    @JsonIgnore
    override fun isNew(): Boolean = super.isNew()
}

// region User

@Entity
class User : Persistable() {

    @Version
    var version: Long = 0

    @LastModifiedDate
    var modifiedDate: Timestamp = Timestamp.from(Instant.now())

    @Column(unique = true)
    lateinit var name: String

    @Transient
    lateinit var password: String
        @JsonIgnore get
        @JsonProperty("password") set

    @JsonIgnore
    lateinit var passwordHash: String

    @Column(unique = true)
    lateinit var email: String
}

// endregion

// region Poll

@Entity
class Poll : Persistable() {
    @Column(unique = true)
    lateinit var description: String

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var options: List<Option> = emptyList()
}

@Projection(name = "nooptions", types = [Poll::class])
interface NoOptionsPoll {
    val description: String
}

@Entity
class Option : Persistable() {
    lateinit var text: String
}

// endregion

// region Vote

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "poll_id"])])
class Vote : Persistable() {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    @ManyToOne(optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    lateinit var poll: Poll

    @ManyToOne(optional = false)
    @JoinColumn(name = "option_id", nullable = false)
    lateinit var option: Option
}

@Projection(name = "detailed", types = [Vote::class])
interface DetailedVote {
    val user: User
    val poll: Poll
    val option: Option
}

// endregion

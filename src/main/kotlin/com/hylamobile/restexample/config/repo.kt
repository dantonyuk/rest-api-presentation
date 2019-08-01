package com.hylamobile.restexample.config

import com.hylamobile.restexample.domain.Option
import com.hylamobile.restexample.domain.Poll
import com.hylamobile.restexample.domain.User
import com.hylamobile.restexample.domain.Vote
import org.springframework.context.support.beans
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import javax.annotation.PostConstruct

fun repoBeans() = beans {
    bean<RepositoryRestConfigurer> {
        object : RepositoryRestConfigurer {
            override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
                config.exposeIdsFor(
                    User::class.java,
                    Poll::class.java,
                    Option::class.java,
                    Vote::class.java)
                super.configureRepositoryRestConfiguration(config)
            }
        }
    }
    bean<Validator>("beforeCreatePollValidator") {
        object : Validator {
            override fun validate(poll: Any, errors: Errors) {
                if (poll is Poll) {
                    val uniqueOptions = setOf(*poll.options.map { it.text }.toTypedArray())
                    if (uniqueOptions.size < poll.options.size) {
                        errors.rejectValue("options", "poll options should be unique")
                    }
                }
            }

            override fun supports(clazz: Class<*>): Boolean = clazz == Poll::class.java
        }
    }
    bean<Validator>("beforeCreateVoteValidator") {
        object : Validator {
            override fun validate(vote: Any, errors: Errors) {
                if (vote is Vote && !vote.poll.options.contains(vote.option)) {
                    errors.rejectValue("option", "option does not belong to the poll")
                }
            }

            override fun supports(clazz: Class<*>): Boolean = clazz == Vote::class.java
        }
    }
    bean<Any> {
        // Workaround for https://jira.spring.io/browse/DATAREST-524
        object {
            @PostConstruct
            fun init() {
                listOf("Poll", "Vote").map { "beforeCreate${it}Validator" }.forEach {
                    ref<ValidatingRepositoryEventListener>().addValidator("beforeCreate", ref(it))
                }
            }
        }
    }
    bean<Any> {
        @RepositoryEventHandler(User::class)
        open class CreateUserHandler(val passwordEncoder: PasswordEncoder) {
            @HandleBeforeCreate
            @Suppress("unused")
            open fun handleUserBeforeCreate(user: User) {
                user.passwordHash = passwordEncoder.encode(user.password)
            }
        }

        CreateUserHandler(ref())
    }
}

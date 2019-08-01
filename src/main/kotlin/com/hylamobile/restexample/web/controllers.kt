package com.hylamobile.restexample.web

import com.hylamobile.restexample.domain.Option
import com.hylamobile.restexample.repo.PollRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.rest.webmvc.BasePathAwareController
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.hateoas.Resources
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@BasePathAwareController
class PollOptionsController {

    @Autowired
    private lateinit var pollRepository: PollRepository

    @GetMapping(path = ["polls/{pollId}/options"])
    fun listOptions(@PathVariable pollId: Long): Resources<Option> {
        val poll = pollRepository.findByIdOrNull(pollId) ?:
            throw ResourceNotFoundException("Poll #$pollId is not found")
        return Resources(poll.options)
    }
}

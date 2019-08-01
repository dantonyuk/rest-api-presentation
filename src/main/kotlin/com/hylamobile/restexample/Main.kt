package com.hylamobile.restexample

import com.hylamobile.restexample.config.securityBeans
import com.hylamobile.restexample.config.repoBeans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration
//import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
//@EnableSwagger2
@EnableWebSecurity
//@Import(SpringDataRestConfiguration::class)
class RestMvcExampleApplication

fun main(args: Array<String>) {
    runApplication<RestMvcExampleApplication>(*args) {
        addInitializers(securityBeans())
        addInitializers(repoBeans())
    }
}

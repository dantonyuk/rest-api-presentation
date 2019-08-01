package com.hylamobile.restexample.config

import com.hylamobile.restexample.domain.User
import com.hylamobile.restexample.repo.UserRepository
import org.springframework.context.support.beans
import org.springframework.core.Ordered
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.WebSecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.userdetails.User as SecurityUser

fun securityBeans() = beans {
    bean<WebSecurityConfigurer<WebSecurity>> {
        object : WebSecurityConfigurerAdapter(), Ordered {
            override fun getOrder(): Int = 1

            override fun configure(http: HttpSecurity) {
                http.csrf().disable()
                    .httpBasic().and()
                    .authorizeRequests()
                        .antMatchers("/error*", "/api", "/api/profile", "/api/profile/**").permitAll()
                        .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .antMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
            }

            override fun configure(auth: AuthenticationManagerBuilder) {
                auth.userDetailsService(ref<UserDetailsService>()).passwordEncoder(ref())
            }
        }
    }
    bean<PasswordEncoder> {
        object : PasswordEncoder {
            override fun encode(rawPassword: CharSequence?): String {
                return BCryptPasswordEncoder().encode(rawPassword)
            }

            override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
                return BCryptPasswordEncoder().matches(rawPassword, encodedPassword)
            }
        }
    }
    bean<UserDetailsService> {
        object : UserDetailsService {
            override fun loadUserByUsername(username: String?): UserDetails? {
                val user: User = ref<UserRepository>().findByName(username) ?: return null
                return SecurityUser(user.name, user.passwordHash, mutableSetOf())
            }
        }
    }
}

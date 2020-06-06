package com.afi.billablehours.security

import com.afi.billablehours.middlewares.JWTAuthEntryPoint
import com.afi.billablehours.middlewares.JWTRequestFilter
import com.afi.billablehours.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableWebSecurity
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    companion object {
        @Bean
        fun passwordEncoder(): BCryptPasswordEncoder {
            return BCryptPasswordEncoder()
        }
    }


    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }


    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Order(1)
    class ApiWebSecurityConfigurationAdapter(jWTAuthEntryPoint: JWTAuthEntryPoint,
                                             userService: UserService,
                                             jWTRequestFilter: JWTRequestFilter) : WebSecurityConfigurerAdapter() {
        private val jWTAuthEntryPoint: JWTAuthEntryPoint
        private val userService: UserService
        private val jWTRequestFilter: JWTRequestFilter

        @Autowired
        @Throws(Exception::class)
        fun configureGlobal(auth: AuthenticationManagerBuilder) {

            // configure AuthenticationManager so that it knows from where to load
            // user for matching credentials
            // Use BCryptPasswordEncoder
            auth.userDetailsService<UserDetailsService>(userService).passwordEncoder(passwordEncoder())
        }

        @Throws(Exception::class)
        override fun configure(http: HttpSecurity) {


            // We don't need CSRF for this example
            http.headers().frameOptions().disable()
            http.csrf().disable() //public routes
                    //        .cors().and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .requestMatchers(EndpointRequest.to("info")).permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                    .antMatchers("/api/v1/auth/**", "/home").permitAll()
                    .antMatchers("/actuator/**").hasRole("ADMIN") // all other requests need to be authenticated
                    .anyRequest().authenticated().and() // make sure we use stateless session; session won't be used to
                    // store user's state.
                    .exceptionHandling().authenticationEntryPoint(jWTAuthEntryPoint).and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


            // Add a filter to validate the tokens with every request
            http.addFilterBefore(jWTRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        }

        init {
            this.jWTAuthEntryPoint = jWTAuthEntryPoint
            this.userService = userService
            this.jWTRequestFilter = jWTRequestFilter
        }
    }
}
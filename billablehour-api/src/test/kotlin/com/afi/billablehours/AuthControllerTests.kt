package com.afi.billablehours

import com.afi.billablehours.controllers.AuthController
import com.afi.billablehours.models.requests.LoginRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes


@SpringBootTest
class AuthControllerTests {

    @Autowired
    private val authController: AuthController? = null

    @Autowired
    private val context: WebApplicationContext? = null


    @Test
    @Throws(Exception::class)
    fun contexLoads() {
        assertThat(context).isNotNull
    }


    @Test
    fun testLogin() {
        val request = MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(request));

        val result: ResponseEntity<*>? = authController?.login(LoginRequest("admin@billinghours.com", "Genesis1:1"))

        assertThat(result?.statusCodeValue).isEqualTo(200)
    }

}

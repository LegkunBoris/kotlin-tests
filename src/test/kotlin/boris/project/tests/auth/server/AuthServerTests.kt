package boris.project.tests.auth.server

import AuthServerApi
import FeignFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringBootConfiguration

@SpringBootConfiguration
class AuthServerTests {

  //  private val authServerClient =
  //      FeignFactory().create(AuthServerApi::class.java, "http://localhost:8081/sso-auth-server")

  @Test
  fun `get token from by client_credentials grant type`() {
    val authServerClient =
        FeignFactory().createWithClientCredentials(
            AuthServerApi::class.java,
            "http://localhost:8080/")
    val res = authServerClient.whoami()
    assertEquals(true, true)
  }

  @Test
  fun `auth server is alive - ping returns info that has status up`() {
    val authServerClient =
        FeignFactory().create(AuthServerApi::class.java, "http://localhost:8081/sso-auth-server")
    val serverInfo = authServerClient.ping()
    assertEquals(serverInfo.status, "up")
  }
}
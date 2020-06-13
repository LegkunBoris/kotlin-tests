package boris.project.tests.auth.server

import AuthServerApi
import FeignFactory
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringBootConfiguration

@SpringBootConfiguration
class AuthServerTests {

  private val authServerClient =
      FeignFactory().create(AuthServerApi::class.java, "http://localhost:8081/sso-auth-server")

  @Test
  fun `jwks endpoint accessible and has keys`() {
    val response = authServerClient.getJwkKeys()
    assert(response.containsKey("keys"))
    val keys = response["keys"] as ArrayList<*>
    assertThat(keys.size > 0)
  }

  @Test
  fun `auth server is alive - ping returns info that server has status up`() {
    val serverInfo = authServerClient.ping()
    assertEquals(serverInfo.status, "up")
  }

  @Test
  fun `auth server returns token on succesfull authorize`() {
    val mapper = ObjectMapper().registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    val (request, response, result) = "http://localhost:8081/sso-auth-server/oauth/token?grant_type=client_credentials&scope=any"
        .httpPost()
        .authentication()
        .basic("boris-client", "boris-secret")
        .responseObject<TokenResponse>(mapper)
    val (token, error) = result
    assert(response.isSuccessful)
    assert(token!!.accessToken.isNotEmpty())
  }
}

class TokenResponse(
  @JsonProperty("access_token") val accessToken: String,
  @JsonProperty("token_type") val tokenType: String,
  @JsonProperty("expires_in") val expires: Int,
  val scope: String, val jti: String
)

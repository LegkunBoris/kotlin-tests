import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails

fun requestInterceptor(): RequestInterceptor {
  return RequestInterceptor { requestTemplate: RequestTemplate ->
    requestTemplate.header("user", "username")
    requestTemplate.header("password", "password")
    requestTemplate.header("Accept", MediaType.APPLICATION_JSON.type)
  }
}

fun oauth2FeignRequestInterceptor(): RequestInterceptor? {
  return OAuth2FeignRequestInterceptor(DefaultOAuth2ClientContext(), resource())
}
fun resource(): OAuth2ProtectedResourceDetails {
  val resourceDetails = ResourceOwnerPasswordResourceDetails()
  resourceDetails.accessTokenUri = "http://localhost:8081/oauth/token"
  resourceDetails.clientId = "boris-client"
  resourceDetails.clientSecret = "boris-secret"
  resourceDetails.grantType = "client_credentials"
  resourceDetails.scope = listOf("any")
  return resourceDetails
}
import boris.project.utils.info.response.Info
import feign.RequestLine

interface AuthServerApi {
  @RequestLine("GET /ping")
  fun ping(): Info

  @RequestLine("GET /oauth/token?grant_type=client_credentials&scope=any")
  fun token(): String
}
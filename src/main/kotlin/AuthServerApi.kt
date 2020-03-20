import boris.project.utils.info.response.Info
import feign.RequestLine

interface AuthServerApi {
  @RequestLine("GET /ping")
  fun ping(): Info

  @RequestLine("GET /whoami")
  fun whoami(): String
}
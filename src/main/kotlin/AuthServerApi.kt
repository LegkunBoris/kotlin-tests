import boris.project.utils.info.response.Info
import feign.RequestLine
import net.minidev.json.JSONObject

interface AuthServerApi {
  @RequestLine("GET /public/ping")
  fun ping(): Info

  @RequestLine("GET /.well-known/jwks.json")
  fun getJwkKeys(): JSONObject
}
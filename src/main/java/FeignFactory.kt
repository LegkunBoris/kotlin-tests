import feign.Feign
import feign.Logger
import feign.Request
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import java.util.concurrent.TimeUnit

class FeignFactory {
  private val connectionTimeout = TimeUnit.SECONDS.toMillis(10).toInt()
  private val readTimeout = TimeUnit.SECONDS.toMillis(20).toInt()

  fun <T> create(clazz: Class<T>, url: String): T {
    return getBaseBuilder()
        .logger(Slf4jLogger(clazz))
        .target(clazz, url)
  }

  fun <T> createWithClientCredentials(clazz: Class<T>, url: String): T {
    return getBaseBuilder()
        .requestInterceptor(oauth2FeignRequestInterceptor())
        .logger(Slf4jLogger(clazz))
        .target(clazz, url)
  }

  fun getBaseBuilder(): Feign.Builder {
    //    val oAuth2FeignRequestInterceptor = KODEIN.instance<OAuth2FeignRequestInterceptor>()
    return Feign.builder()
        .client(OkHttpClient())
        .encoder(GsonEncoder())
        .decoder(GsonDecoder())
        .options(Request.Options(connectionTimeout, readTimeout))
        .logLevel(Logger.Level.FULL)
    //        .requestInterceptor(oAuth2FeignRequestInterceptor)
  }
}
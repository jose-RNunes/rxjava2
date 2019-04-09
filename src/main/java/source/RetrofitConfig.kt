package source

import io.reactivex.Observable
import model.Film
import model.Films
import model.People
import model.Peoples
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path


class RetrofitConfig {

    companion object {

        private const val BASE_URL = "https://swapi.co/api/"
        private val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp())

        private val retrofit = retrofitBuilder.build()

        private val apiService = retrofit.create(ApiService::class.java)

        fun getApiService(): ApiService = apiService

        private fun okHttp():OkHttpClient{
            var logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                .addInterceptor(logging).build()
        }

    }




}

interface ApiService {

    @GET("people/{idPeople}/")
    fun getPeople(@Path("idPeople") idPeople: Int):Observable<People>

    @GET("people")
    fun getPeoples():Observable<Peoples>

    @GET("films/{idFilm}")
    fun getPeopleMovie(@Path("idFilm")idFilm:Int):Observable<Film>

    @GET("films")
    fun getMovies():Observable<Films>


}
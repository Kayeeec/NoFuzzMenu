package cz.muni.fi.nofuzzmenu.zomato

import cz.muni.fi.nofuzzmenu.zomato.models.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ZomatoApiService {

    @GET("locations")
    fun getLocations(
        @Query("user-key") apiKey: String,
        @Query("query") query: String?,
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("count") count: Int?
    ) : Call<ZomatoLocation>

    //https://developers.zomato.com/api/v2.1/dailymenu?res_id=19080841
    @GET("dailymenu")
    fun getMenu(
        @Header("user_key") apiKey: String,
        @Query("res_id") restaurantId: String?
    ): Call<ZomatoMenu>

    @GET("establishments")
    fun getEstablishmentTypes(
        @Header("user-key") apiKey: String,
        @Query("city_id") cityId: String?,
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?
    ): Call<List<ZomatoEstablishmentType>>


    @GET("search")
    fun getRestaurants(
        @Header("user-key") apiKey: String,
        @Query("q") query: String?,
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("radius") radius: Double?, // does not work too well, will have to filter manually
        @Query("cuisines") cuisines: String?, // comma-separated IDs
        @Query("sort") sortBy: String?, // TODO use enum, to string: cost, rating, real_distance (dist works well)
        @Query("order") sortOrder: String?, // asc/desc
        @Query("start") start: Int?,
        @Query("count") count: Int?
    ) : Call<ZomatoRestaurantsListResponse>

    @GET("search")
    fun getRestaurantsAsync(
        @Header("user-key") apiKey: String,
        @Query("q") query: String?,
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("radius") radius: Double?, // does not work too well, will have to filter manually
        @Query("cuisines") cuisines: String?, // comma-separated IDs
        @Query("sort") sortBy: String?, // cost, rating, real_distance
        @Query("order") sortOrder: String?, // asc/desc
        @Query("start") start: Int?,
        @Query("count") count: Int?
    ) : Deferred<Response<ZomatoRestaurantsListResponse>>

    //https://developers.zomato.com/api/v2.1/dailymenu?res_id=19080841
    @GET("dailymenu")
    fun getMenuAsync(
        @Header("user_key") apiKey: String,
        @Query("res_id") restaurantId: String?
    ): Deferred<Response<ZomatoDailyMenuResponse>>


}

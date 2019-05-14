package cz.muni.fi.nofuzzmenu.zomato

import cz.muni.fi.nofuzzmenu.zomato.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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

    @GET("dailymenu/{restaurantId}")
    fun getMenu(
        @Header("user_key") apiKey: String,
        @Path("restaurantId") restaurantId: String?
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


}

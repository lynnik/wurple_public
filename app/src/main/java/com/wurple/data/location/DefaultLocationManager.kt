package com.wurple.data.location

import android.content.Context
import android.content.pm.PackageManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.wurple.R
import com.wurple.domain.extension.empty
import com.wurple.domain.location.LocationManager
import com.wurple.domain.model.location.Location
import com.wurple.domain.model.location.LocationSearch
import kotlinx.coroutines.tasks.await

class DefaultLocationManager(
    private val context: Context
) : LocationManager {
    private val placesClient: PlacesClient by lazy { Places.createClient(context) }
    private var token = getAutocompleteSessionToken()

    init {
        val apiKey = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            .metaData
            .getString("com.google.android.geo.API_KEY") ?: String.empty
        Places.initialize(context, apiKey)
    }

    override suspend fun searchForLocation(query: String): List<LocationSearch> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.CITIES)
            .setSessionToken(token)
            .setQuery(query)
            .build()
        val response: FindAutocompletePredictionsResponse =
            placesClient.findAutocompletePredictions(request).await()
        return response.autocompletePredictions
            .map { prediction ->
                LocationSearch(
                    id = prediction.placeId,
                    primaryText = prediction.getPrimaryText(null).toString(),
                    secondaryText = prediction.getSecondaryText(null).toString()
                )
            }
    }

    override suspend fun getLocationById(id: String): Location {
        token = getAutocompleteSessionToken()
        val placeFields = listOf(Place.Field.ID, Place.Field.ADDRESS_COMPONENTS)
        val request = FetchPlaceRequest.newInstance(id, placeFields)
        val response = placesClient.fetchPlace(request).await()
        val addressComponents = response.place.addressComponents?.asList()
        val locationId = response.place.id ?: String.empty
        val city = addressComponents
            ?.find { addressComponent -> addressComponent.types.contains("locality") }
            ?.name
            ?: String.empty
        val country = addressComponents
            ?.find { addressComponent -> addressComponent.types.contains("country") }
            ?.name
            ?: String.empty
        if (locationId.isEmpty() || city.isEmpty() || country.isEmpty()) {
            throw IllegalStateException(context.getString(R.string.location_incorrect))
        }
        return Location(
            id = locationId,
            city = city,
            country = country,
            formattedLocation = context.getString(
                R.string.data_user_formatted_location,
                city,
                country
            )
        )
    }

    private fun getAutocompleteSessionToken(): AutocompleteSessionToken {
        return AutocompleteSessionToken.newInstance()
    }
}
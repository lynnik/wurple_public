package com.wurple.domain.location

import com.wurple.domain.model.location.Location
import com.wurple.domain.model.location.LocationSearch

interface LocationManager {
    suspend fun searchForLocation(query: String): List<LocationSearch>
    suspend fun getLocationById(id: String): Location
}
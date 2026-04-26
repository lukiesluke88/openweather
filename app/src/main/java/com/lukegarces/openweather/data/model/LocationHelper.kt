package com.lukegarces.openweather.data.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationHelper(
    private val context: Context
) {
    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getCurrentLocationInfo(): LocationInfo {
        val hasPermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            throw SecurityException("Location permission not granted")
        }

        val location = suspendCancellableCoroutine { continuation ->
            fusedLocationClient
                .getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                )
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(location)
                    } else {
                        continuation.resumeWithException(
                            Exception("Unable to get current location")
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )

        val address = addresses?.firstOrNull()

        return LocationInfo(
            coord = Coord(
                lon = location.longitude,
                lat = location.latitude
            ),
            city = address?.locality ?: address?.subAdminArea ?: "Unknown city",
            country = address?.countryName ?: "Unknown country"
        )
    }
}

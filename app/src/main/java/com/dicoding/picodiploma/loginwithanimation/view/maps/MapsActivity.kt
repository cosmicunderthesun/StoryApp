package com.dicoding.picodiploma.loginwithanimation.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dicoding.picodiploma.loginwithanimation.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel by viewModels<MapsViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.getSession().observe(this) { user ->
            viewModel.getStoryWithLocation(user.token.toString())
        }

        viewModel.storyResponse.observe(this) { story ->
            story.forEach { listStoryItem ->
                val lat = listStoryItem.lat ?: 0.0
                val lon = listStoryItem.lon ?: 0.0
                val desc = listStoryItem.description ?: ""

                val location = LatLng(lat,lon)

                mMap.addMarker(MarkerOptions().position(location).title(desc))
            }
            val indonesia = LatLng(6.1750,106.8283)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(indonesia))
        }

    }
}
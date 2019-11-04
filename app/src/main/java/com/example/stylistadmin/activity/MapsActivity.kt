package com.example.stylistadmin.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.stylistadmin.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnCameraIdleListener {
private lateinit var googleApiClient:GoogleApiClient
    private lateinit var mMap: GoogleMap

    private var REQUEST_PERMISSION_VALUE = 4321
    private  var lat:Double=0.0
    private var lng:Double=0.0
    private var short_location:String?=""
    private var city: String = ""
    private var requestQueue: RequestQueue?=null
    private var stringValue = Array(3){""}
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val TAG = "MPA"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        btnContinueToHome.setOnClickListener {
            var intent=Intent()
            intent.putExtra("long_address",etFullAddress.text.toString()+"\n"+city)
            intent.putExtra("lat",lat)
            intent.putExtra("lng",lng)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.setOnCameraIdleListener(this)
        if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_VALUE
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
            ) {
                getGoogleServiceLocation()
                mMap.isMyLocationEnabled = true
            }
        }
    }

    private fun getGoogleServiceLocation() {


        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(p0: Bundle?) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)
                        if (ActivityCompat.checkSelfPermission(
                                        this@MapsActivity,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                        this@MapsActivity,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                    this@MapsActivity,
                                    arrayOf(
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                            android.Manifest.permission.ACCESS_FINE_LOCATION
                                    ),
                                    REQUEST_PERMISSION_VALUE
                            )

                        }
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            if (it != null) {
                                var currentLocation = LatLng(it.latitude, it.longitude)
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
                                getAddresses(currentLocation.latitude, currentLocation.longitude)
                            }
                        }
                    }

                    override fun onConnectionSuspended(p0: Int) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
                .addOnConnectionFailedListener {
                    Toast.makeText(this@MapsActivity, "Location is not on", Toast.LENGTH_SHORT).show();
                }
                .addApi(LocationServices.API)
                .build()
        googleApiClient.connect()

    }

    private fun getPlaceName(latitude:Double,longitude:Double):String {
        var address: Address


        Log.d(TAG, "getPlaceName: $latitude   $longitude")
        var geocoder = Geocoder(this)
        try {
            var addresses:List<Address> = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.isNotEmpty())
            {address = addresses[0]
                city= address.getAddressLine(0);}
            else
                city= "";

        } catch ( e: IOException) {
            e.printStackTrace()
        }
        return city;
    }
    fun getAddresses(lattitude: Double?, longitude: Double?): String {

        val url = ("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lattitude + "," + longitude
                + "&key=AIzaSyCMqCrBrWu4jAM32E-ZZpd498sGg3hRubk")
        val request = StringRequest(url, { string ->
            // Log.d(TAG, "onResponse: "+string);
            stringValue = parseJsonData(string)
            city = stringValue[0]
            short_location = stringValue[1]

            Log.d(TAG, "getAddresses: $city")
            tvLocation.text = city
//            Signin.sendingLocation = city
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lattitude!!, longitude!!), 18f))
        }, { volleyError -> Toast.makeText(this, "Some error occurred!!$volleyError", Toast.LENGTH_SHORT).show() })
        if (requestQueue==null)
            requestQueue = Volley.newRequestQueue(this)
        requestQueue!!.add(request)
        return city
    }

    private fun parseJsonData(jsonString: String): Array<String> {

        try {
            val `object` = JSONObject(jsonString)
            val results = `object`.getJSONArray("results")
            val ob = results.get(0) as JSONObject
            val data = ob.getJSONArray("address_components")
            val short_Location = data.getJSONObject(1).getString("short_name")
            stringValue[0] = ob.getString("formatted_address")
            stringValue[1] = short_Location
            stringValue[2] = data.getJSONObject(data.length() - 1).getString("short_name")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return stringValue
    }
    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                mMap.isMyLocationEnabled = true
            } else if (resultCode == Activity.RESULT_CANCELED) {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSION_VALUE
                )
            }
        }

    }

    override fun onCameraIdle() {
        var  cityAddress = getPlaceName(mMap.cameraPosition.target.latitude, mMap.cameraPosition.target.longitude)
        lat=mMap.cameraPosition.target.latitude
        lng=mMap.cameraPosition.target.longitude
        tvLocation.text = cityAddress
    }

}

package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.R.style.Theme_MyApplication
import com.example.myapplication.database.CountriesData
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.mainScreen.MainScreenFragmentDirections
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MainActivity : AppCompatActivity() {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var viewModel_main:Main_ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setTheme(Theme_MyApplication)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        val toolbar:Toolbar= binding.toolbar2  //findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navfrag=supportFragmentManager.findFragmentById(R.id.navHostFragmentFragment)  as NavHostFragment
        val navControl = navfrag.navController
        NavigationUI.setupActionBarWithNavController(this, navControl)

        binding.lifecycleOwner=this


        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)


        //setupActionBarWithNavController(this,navControl)

        //val fusedLocation : FusedLocationProviderClient=LocationS\

/*        val apiKey:String = getString(R.string.api_key)

        if(!Places.isInitialized()){
            Places.initialize(applicationContext, apiKey)
        }

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete) as AutocompleteSupportFragment?
        autocompleteFragment!!.setTypeFilter(TypeFilter.CITIES)
        autocompleteFragment.setPlaceFields(
            Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Toast.makeText(applicationContext, place.name, Toast.LENGTH_SHORT).show()
                lat.value = place.latLng?.latitude.toString()
                lon = place.latLng?.longitude.toString()

            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Toast.makeText(applicationContext, status.toString(), Toast.LENGTH_LONG).show()
            }
        })

 */

        viewModel_main=ViewModelProvider(this).get(Main_ViewModel::class.java)

        lat.observe(this, Observer { viewModel_main.invokee() })

        binding.weather=viewModel_main

        binding.layoutChange.setOnClickListener{
            layout_num.value = layout_num.value?.plus(1)
        }

        binding.imageButton.setOnClickListener{
            binding.navHostFragmentFragment.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToSearch())
        }

        /*try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
            else{
                viewModel.getLocationACCESS_FINE_LOCATION()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }*/


        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        if(!checkPermissions()) {
            requestPermissions()
        }
        else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this){
            if(it.isSuccessful && it.result!=null) {
                lastLocation=it.result
                lat.value = lastLocation!!.latitude.toString()
                lon = lastLocation!!.longitude.toString()
                Log.i("location","$lat $lon")
            }
            else {
                Log.i("location exception",it.exception.toString())
                Toast.makeText(this,"Make sure location is turned on",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String, listener: View.OnClickListener) {
        //Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
        Snackbar.make(findViewById(R.id.main_layout),mainTextStringId,Snackbar.LENGTH_INDEFINITE).setAction(actionStringId,listener)
    }

    fun checkPermissions():Boolean{
        val permissionState = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState==PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationRequestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions(){
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)
        if (shouldProvideRationale){
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationRequestPermission()
                })
        }
        else {
            startLocationRequestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode== REQUEST_PERMISSIONS_REQUEST_CODE){
            when {
                grantResults.isEmpty()->{
                    Log.i("permssion" , "interrupted")
                }
                grantResults[0]==PackageManager.PERMISSION_GRANTED->{
                    getLastLocation()
                }
                else->{
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navfrag=supportFragmentManager.findFragmentById(R.id.navHostFragmentFragment)  as NavHostFragment
        val navControl = navfrag.navController
        return navControl.navigateUp()
    }
    companion object{
        val recent : MutableList<CountriesData> = ArrayList()
        var layout_num = MutableLiveData<Int>(0)
        val REQUEST_PERMISSIONS_REQUEST_CODE=34
        var lastLocation : Location? = null
        var lat= MutableLiveData<String>()
        var lon = "-2.15"
        var appId= "85b62abb7edf05d1a035c3ce4f23a082"
    }
}
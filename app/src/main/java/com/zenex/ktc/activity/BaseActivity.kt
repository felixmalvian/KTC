package com.zenex.ktc.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.zenex.ktc.R
import com.zenex.ktc.data.UserData
import com.zenex.ktc.databinding.ActivityBaseBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    var userData: UserData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        userData = intent.getSerializableExtra("data") as UserData
        userData?.getSiteList(this)

        navController = navHostFragment.navController
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        val graph = inflater.inflate(R.navigation.nav_whatever)
        navController.setGraph(R.navigation.nav_graph)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = this.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                this.currentFocus?.windowToken,
                0
            )
        }
    }


    private var imageUri: Uri? = null

    fun openCamera(fragment: Fragment, id: Int) {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, id)
        openCameraInterface(fragment, id)
    }

    private fun openCameraInterface(fragment: Fragment, id: Int) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Attachment")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Att Desc")
        imageUri = this.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)// Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)// Launch intent
        startActivityForResult(intent, id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)// Callback from camera intent
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 1001){
                val currentFragment = navController.currentDestination?.id
                if (currentFragment == R.id.createFaultReportFragment){

                }
            }
        }
        else {
            // Failed to take picture
//            showAlert(“Failed to take camera picture”)
        }
    }


}




















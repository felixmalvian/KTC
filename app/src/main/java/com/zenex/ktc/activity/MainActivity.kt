package com.zenex.ktc.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.ui.AppBarConfiguration
import com.zenex.ktc.api.RetrofitClient
import com.zenex.ktc.api.param.input.ParamLogin
import com.zenex.ktc.api.param.response.ParamLoginResponse
import com.zenex.ktc.data.UserData
import com.zenex.ktc.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.clMain.setOnClickListener{
            hideSoftKeyboard(this)
            currentFocus?.clearFocus()
        }

        binding.btnLogin.setOnClickListener{
            login()

        }

        setContentView(binding.root)

    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                0
            )
        }
    }

    private fun login() {
        binding.btnLogin.visibility = GONE
        binding.progressCircle.visibility = VISIBLE

        val paramLogin = ParamLogin()
        paramLogin.username = binding.tilUsername.editText?.text.toString()
        paramLogin.password = binding.tilPassword.editText?.text.toString()

        val request = RetrofitClient.instance.doLogin(paramLogin)
        request.enqueue(object: Callback<ParamLoginResponse> {
            override fun onResponse(
                call: Call<ParamLoginResponse>,
                response: Response<ParamLoginResponse>
            ) {
                if(response.code() != 200){
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Error")
                        .setMessage(response.code().toString())
                        .show()
                } else {
                    val body = response.body()!!
                    if (body.data?.size!! == 0){
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Login Failed")
                            .setMessage("Username or Password incorrect. Please try again.")
                            .show()
                        binding.btnLogin.visibility = VISIBLE
                        binding.progressCircle.visibility = GONE
                        return
                    }
                    val data = body.data?.get(0)
                    val userData = UserData()
                    if (data != null) {
                        userData.AC_Username =  data.AC_Username
                        userData.AC_LoginName =  data.AC_LoginName
                        userData.UR_UserRole = data.UR_UserRole
                        userData.EMP_No = data.EMP_No

                        val intent = Intent(applicationContext, BaseActivity::class.java)
                        intent.putExtra("data", userData)
                        startActivity(intent)
                    }
                    binding.btnLogin.visibility = VISIBLE
                    binding.progressCircle.visibility = GONE
                }
            }

            override fun onFailure(call: Call<ParamLoginResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Error")
                        .setMessage(t.message)
                        .show()

                    binding.progressCircle.isVisible = false
                    binding.btnLogin.isVisible = true
                }
            }

        })
    }

}
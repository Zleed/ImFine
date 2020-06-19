package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity() {

    private var phone: String? = null
    private var message: String? = "I'm fine!"

    private var settingFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logo.visibility = View.GONE
        requestPermission()
        loadData()
        btnSettings()
        btnSend()
        btnSave()
        btnClose()
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 123)
        }
    }

    private fun btnSend() {
        btn_send.setOnClickListener {
            val smsManager = SmsManager.getDefault()
            try {
                smsManager.sendTextMessage(phone, null, message, null, null)
                btn_send.visibility = View.GONE
                btn_setting.visibility = View.GONE
                btn_close.visibility = View.GONE
                grandmaAnimation()
                exit(3000)
            } catch (e: Exception) {
                showSettings()
            }
        }
    }

    private fun exit(delay: Long) {
        Handler().postDelayed(Runnable {
            finishAndRemoveTask()
        }, delay)
    }

    private fun grandmaAnimation() {
        logo.visibility = View.VISIBLE
        val d = logo.drawable
        if (d is AnimatedVectorDrawable) {
            d.start()
        }
    }

    private fun btnSettings() {
        btn_setting.setOnClickListener {
            if (settingFlag)
                showSettings()
            else
                showMainMenu()
        }
    }

    private fun showSettings() {
        input_phone.visibility = View.VISIBLE
        input_message.visibility = View.VISIBLE
       btn_save.visibility = View.VISIBLE
        btn_send.visibility = View.GONE
        settingFlag = false
    }

    private fun showMainMenu() {
        btn_send.visibility = View.VISIBLE
        btn_save.visibility = View.GONE
        input_phone.visibility = View.GONE
        input_message.visibility = View.GONE
        settingFlag = true
    }

    private fun saveData() {
        phone = input_phone.text.toString()
        message = input_message.text.toString()
        val sharedPreferences = getSharedPreferences("ImFine", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("phone", phone)
            putString("message", message)
        }.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("ImFine", Context.MODE_PRIVATE)
        phone = sharedPreferences.getString("phone", null)
        message = sharedPreferences.getString("message", "I'm fine!")
        if (phone != null) {
            input_phone.setText(phone)
            input_message.setText(message)
            showMainMenu()
        }
        else showSettings()
    }

    private fun btnSave(){
        btn_save.setOnClickListener {
            saveData()
            showMainMenu()
        }
    }

    private fun btnClose() {
        btn_close.setOnClickListener {
            if (settingFlag) {
                exit(0)
            }
            else {
                showMainMenu()
            }
        }
    }
}

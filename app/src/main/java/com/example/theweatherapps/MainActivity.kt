package com.example.theweatherapps

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var watherText: TextView
    private lateinit var button: Button

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        watherText = findViewById<TextView>(R.id.watherText)
        button = findViewById<Button>(R.id.weatherButton)
        //位置情報の権限リクエスト
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        button.setOnClickListener {
            getCurrentLocationWeather()//API叩いて現在地取得する関数
        }

    }

    private fun getCurrentLocationWeather() {

        val apiKey = "7592dc4e30538dde8714be927737b08f"
        val url =
            "http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid=${apiKey}"

        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                //APIのレスポンス
                val weatherDisctiptions =
                    response.getJSONArray("weather").getJSONObject(0).getString("discription")
                watherText.text = "現在の天気: $weatherDisctiptions"
            },
            { error ->
                //取得失敗
                watherText.text = "取得に失敗しました😔"
                Log.e("API Error", "APIからのレスポンス取得中にエラーが発生しました: ${error.message}")
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
}
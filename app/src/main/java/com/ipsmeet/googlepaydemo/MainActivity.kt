package com.ipsmeet.googlepaydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ipsmeet.googlepaydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    private val GOOGLE_PAY_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPayWithGPay.setOnClickListener {
            payViaGPay()
        }
    }

    private fun payViaGPay() {
        val uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", getString(R.string.receiver_upi_id)) // UPI-ID of business account only
            .appendQueryParameter("pn", getString(R.string.receiver_name))
            .appendQueryParameter("am", "1")
            .appendQueryParameter("cu", "INR")
            .appendQueryParameter("tn", "testing")
            .appendQueryParameter("mc", System.currentTimeMillis().toString())
            .appendQueryParameter("tr", System.currentTimeMillis().toString())
            .build()

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(uri)
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
        startActivity(intent)
    }
}
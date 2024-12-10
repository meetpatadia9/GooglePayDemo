package com.ipsmeet.googlepaydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
        try {
            val uri = Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", getString(R.string.receiver_upi_id)) // Business UPI ID
                .appendQueryParameter("pn", getString(R.string.receiver_name))  // Payee Name
                .appendQueryParameter("am", "1")                                // Amount
                .appendQueryParameter("cu", "INR")                              // Currency
                .appendQueryParameter("tn", "Test Payment")                     // Note
                .appendQueryParameter("tr", System.currentTimeMillis().toString()) // Transaction ID
                .build()

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)

//            if (packageManager.resolveActivity(intent, 0) != null) {
                startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE)
//            } else {
//                showToast("Google Pay is not installed.")
//            }
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            if (data != null) {
                val response = data.getStringExtra("response")
                parseUPIResponse(response)
            } else {
                // User canceled the transaction
                showToast("Payment canceled by user.")
            }
        }
    }

    private fun parseUPIResponse(response: String?) {
        response?.let {
            val responseParams = Uri.parse(it)
            val status = responseParams.getQueryParameter("Status")
            if ("SUCCESS".equals(status, true)) {
                showToast("Payment successful.")
            } else {
                val reason = responseParams.getQueryParameter("reason")
                showToast("Payment failed: $reason")
            }
        } ?: showToast("Invalid response.")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
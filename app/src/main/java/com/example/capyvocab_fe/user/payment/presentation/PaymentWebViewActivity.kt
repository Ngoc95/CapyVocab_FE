package com.example.capyvocab_fe.user.payment.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity

class PaymentWebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orderUrl = intent.getStringExtra("orderUrl") ?: ""
        val webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (it.contains("/orders/vnpay-return")) {
                        val uri = Uri.parse(it)
                        val vnpParams = listOf(
                            "vnp_Amount", "vnp_BankCode", "vnp_BankTranNo", "vnp_BankTranNo", "vnp_CardType",
                            "vnp_OrderInfo", "vnp_PayDate", "vnp_ResponseCode", "vnp_TmnCode", "vnp_TransactionNo",
                            "vnp_TransactionStatus", "vnp_TxnRef", "vnp_SecureHash"
                        )
                        val resultIntent = Intent().apply {
                            for (param in vnpParams) {
                                putExtra(param, uri.getQueryParameter(param))
                            }
                            // Also keep isSuccess and orderId for compatibility if needed
                            putExtra("isSuccess", uri.getQueryParameter("isSuccess") ?: "false")
                            putExtra("orderId", uri.getQueryParameter("orderId"))
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                        return true
                    }
                }
                return false
            }
        }
        webView.loadUrl(orderUrl)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có chắc chắn muốn hủy thanh toán?")
            .setPositiveButton("Có") { _, _ ->
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            .setNegativeButton("Không", null)
            .show()
    }
} 
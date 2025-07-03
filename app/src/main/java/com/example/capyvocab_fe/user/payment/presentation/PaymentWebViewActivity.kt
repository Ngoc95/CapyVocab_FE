package com.example.capyvocab_fe.user.payment.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import com.example.capyvocab_fe.R

class PaymentWebViewActivity : ComponentActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orderUrl = intent.getStringExtra("orderUrl") ?: ""
        webView = WebView(this)
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
                            "vnp_TransactionStatus", "vnp_TxnRef", "vnp_SecureHash", "isSuccess", "orderId"
                        )
                        val resultIntent = Intent().apply {
                            for (param in vnpParams) {
                                putExtra(param, uri.getQueryParameter(param))
                            }
                            // Also keep isSuccess and orderId for compatibility if needed
                            putExtra("isSuccess", uri.getQueryParameter("isSuccess") ?: "false")
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

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn hủy thanh toán?")
                .setPositiveButton("Có") { _, _ ->
                    val orderId = intent.getStringExtra("orderId")
                    if (orderId != null) {
                        val resultIntent = Intent().apply {
                            putExtra("action", "cancel_order")
                            putExtra("orderId", orderId)
                        }
                        setResult(Activity.RESULT_CANCELED, resultIntent)
                    } else {
                        setResult(Activity.RESULT_CANCELED)
                    }
                    finish()
                }
                .setNegativeButton("Không", null)
                .show()
        }
    }
} 
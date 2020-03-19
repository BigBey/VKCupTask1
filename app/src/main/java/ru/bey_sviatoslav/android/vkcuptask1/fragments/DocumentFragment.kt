package ru.bey_sviatoslav.android.vkcuptask1.fragments

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.fragment.app.Fragment
import ru.bey_sviatoslav.android.vkcuptask1.MainActivity
import ru.bey_sviatoslav.android.vkcuptask1.R
import ru.bey_sviatoslav.android.vkcuptask1.models.VKDocument


class DocumentFragment(private val vkDocument: VKDocument) : Fragment() {

    private val webViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }
    private lateinit var webView : WebView
    private lateinit var backImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_document, container, false)
        backImageView = view.findViewById(R.id.backImageView)
        backImageView.setOnClickListener {
            (activity as MainActivity).hideDocument()
        }
        webView = view.findViewById(R.id.webView)
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        webView.setWebViewClient(WebViewClient())
        when(vkDocument.type) {
            1 -> {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=${vkDocument.url}")
            }
            3,4 -> {
                webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                webView.setScrollbarFadingEnabled(false);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                webView.getSettings().setAllowFileAccess(true);
                webView.loadUrl(vkDocument.url)
            }
            5 -> {
                webView.settings.setMediaPlaybackRequiresUserGesture(false)
                webView.loadUrl(vkDocument.url)
            }
            6 -> {
                val intent = Intent(Intent.ACTION_VIEW) //I encourage using this instead of specifying the string "android.intent.action.VIEW"
                intent.setDataAndType(Uri.parse(vkDocument.url), "video/3gpp")
                view.getContext().startActivity(intent)
            }
            else -> {
                webView.loadUrl(vkDocument.url)
            }
        }
        return view
    }
}

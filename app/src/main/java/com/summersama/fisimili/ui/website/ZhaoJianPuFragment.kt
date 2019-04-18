package com.summersama.fisimili.ui.website

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

import com.summersama.fisimili.R
import com.summersama.fisimili.utils.ConstantUtils
import kotlinx.android.synthetic.main.zhao_jian_pu_fragment.*

class ZhaoJianPuFragment : Fragment() {

    companion object {
        fun newInstance() = ZhaoJianPuFragment()
    }

    private lateinit var viewModel: ZhaoJianPuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.zhao_jian_pu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ZhaoJianPuViewModel::class.java)
        initWebView()
        val url = ConstantUtils.ZHAOJIANPU_URL
        zjp_main_wv.loadUrl(url)
        zjp_main_wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                Log.d("跳转的url",url)
                return true
            }
        }
    }
    val newUA= "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN";
    private fun initWebView() {

        zjp_main_wv.settings.userAgentString=newUA
        zjp_main_wv.settings.cacheMode= WebSettings.LOAD_NO_CACHE
        zjp_main_wv.clearCache(true)
        zjp_main_wv.clearHistory()
        zjp_main_wv.settings.javaScriptEnabled = true;
    }
}

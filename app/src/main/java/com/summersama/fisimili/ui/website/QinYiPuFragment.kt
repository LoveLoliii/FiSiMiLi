package com.summersama.fisimili.ui.website

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

import com.summersama.fisimili.R
import com.summersama.fisimili.utils.ConstantUtils
import kotlinx.android.synthetic.main.qin_yi_pu_fragment.*

class QinYiPuFragment : Fragment() {

    companion object {
        fun newInstance() = QinYiPuFragment()
    }

    private lateinit var viewModel: QinYiPuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.qin_yi_pu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QinYiPuViewModel::class.java)
        initWebView()
        val url = ConstantUtils.QINYIPU_URL
        qyp_main_wv.webViewClient= WebViewClient()
        qyp_main_wv.loadUrl(url)
        /*qyp_main_wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                Log.d("跳转的url",url)
                return true
            }
        }*/
        qyp_main_wv .setOnKeyListener(object :View.OnKeyListener {
            override fun onKey(view:View, keyCode:Int, keyEvent:KeyEvent) :Boolean{
                if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
                        if (qyp_main_wv.canGoBack()){
                            qyp_main_wv.goBack();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    val newUA= "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN";
    private fun initWebView() {

        qyp_main_wv.settings.userAgentString=newUA
        qyp_main_wv.settings.cacheMode= WebSettings.LOAD_CACHE_ELSE_NETWORK
        qyp_main_wv.clearCache(true)
        //qyp_main_wv.clearHistory()
        qyp_main_wv.settings.javaScriptEnabled = true;
    }
}

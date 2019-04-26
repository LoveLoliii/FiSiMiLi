package com.summersama.fisimili.ui.website

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import com.summersama.fisimili.R
import com.summersama.fisimili.utils.ConstantUtils
import com.summersama.fisimili.utils.FNApplication
import com.summersama.fisimili.utils.FUtils
import kotlinx.android.synthetic.main.qin_yi_pu_fragment.*
import java.lang.Exception

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
    var title = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QinYiPuViewModel::class.java)
        initWebView()


        val bundle = arguments
        var url = ""
        try {
             url = bundle!!.getString("url")!!
            if (url ==""){
                url = ConstantUtils.QINYIPU_URL
            }

        }catch (e:Exception){
            url = ConstantUtils.QINYIPU_URL
        }

        qyp_main_wv.webViewClient= WebViewClient()
        qyp_main_wv.webChromeClient = object : WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, title: String?) {
                getWebTitle()
            }
        }

        qyp_main_wv.loadUrl(url)
        qyp_main_wv .setOnKeyListener(object :View.OnKeyListener {
            override fun onKey(view:View, keyCode:Int, keyEvent:KeyEvent) :Boolean{
                if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
                        if (qyp_main_wv.canGoBack()){
                            qyp_main_wv.goBack();
                            getWebTitle();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        qypf_collection_fb.setOnClickListener {
            Toast.makeText(context,"url:${qyp_main_wv.url}",Toast.LENGTH_SHORT).show()
            var urlC = FUtils().getToken(FNApplication.getContext(),"url_collect")
            if (urlC.contains(qyp_main_wv.url)){
                    Toast.makeText(context,"曾经收藏过",Toast.LENGTH_SHORT).show()
            }else{
                urlC +="*${qyp_main_wv.url}@$title"
                FUtils().saveToken(FNApplication.getContext(),"url_collect",urlC)
                qypf_collection_fb.setBackgroundColor(resources.getColor(R.color.枣红))
                Toast.makeText(context,"收藏完毕,可在左侧收藏菜单中查看",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWebTitle() {
        var forwardList  = qyp_main_wv.copyBackForwardList()
        var item = forwardList.currentItem
        if (item!=null){
            Toast.makeText(context,item.title,Toast.LENGTH_SHORT).show()
            title = item.title
        }

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

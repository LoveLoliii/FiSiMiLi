package com.summersama.fisimili.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.room.util.StringUtil
import com.google.gson.Gson

import com.summersama.fisimili.R
import com.summersama.fisimili.SearchActivity
import com.summersama.fisimili.utils.ConstantUtils
import com.summersama.fisimili.utils.FApplication
import com.summersama.fisimili.utils.FUtils
import com.summersama.fisimili.utils.OkHttpUtil
import kotlinx.android.synthetic.main.git_hub_login_fragment.*
import kotlinx.android.synthetic.main.search_activity.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder


class GitHubLoginFragment : Fragment() {

    companion object {
        fun newInstance() = GitHubLoginFragment()
    }

    private lateinit var viewModel: GitHubLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.git_hub_login_fragment, container, false)
    }
    lateinit var   navController:NavController//as NavHostFragment
    val callback = "https://summersama.com/FSML/callback"
    val client_id = "d40e25b8c651b9d415f3"
    val newUA= "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36";
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GitHubLoginViewModel::class.java)
        navController = findNavController()
        // 检查token
        val check = FUtils().getToken(this.context!!,"access_token")
        if (check != "" && check != "bad_verification_code"){
            navController.navigate(R.id.searchFragment)
            Toast.makeText(activity,"已登陆！",Toast.LENGTH_SHORT).show()
            return
        }else{
            // 处理bundle
            var code = arguments?.getString("url")
            val token = arguments?.getString("token")
            if (token!=null){
                Log.d("token",token)
                val x = token.split("&")

                // 保存token
                FUtils().saveToken(this.activity!!,"access_token",x[0].split("=")[1])
                Toast.makeText(activity,"登陆成功！",Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.searchFragment)
            }else if (code != null && code !=""){
                code = code.substring(code.indexOf("code")+5 )
                Log.d("code:",code)
                // 请求access token
                initWebView()
                /*ghlf_login_wv.webViewClient = object : WebViewClient() {
                   override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                       view.loadUrl(url)
                       Log.d("access token ",url)
                       var p=Bundle()
                       p.putString("token",url)
                       val navController = findNavController()//as NavHostFragment
                       navController.navigate(R.id.github_login,p)
                       return true
                   }
               }*/
                val client_secret = "d39a7ce11e3f7d6424bdbde239deff43a4b5b551"
                val url = ConstantUtils.GITHUB_ACCESS_TOKEN_URL+"access_token"
                val params = "redirect_uri=$callback&client_id=$client_id&code=$code&client_secret=$client_secret"
                val mapParams = HashMap<String,String>(4)
                mapParams.put("redirect_uri",callback)
                mapParams.put("client_id",client_id)
                mapParams.put("code",code)
                mapParams.put("client_secret",client_secret)
                val g = Gson()
                val ps = g.toJson(mapParams)
                //ghlf_login_wv.postUrl(url, ps.toByteArray())
                //ghlf_login_wv.postUrl(url, EncodingUtils.getBytes(params, "BASE64"));
                OkHttpUtil.post(mapParams,url).enqueue(object :Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val rs = response.body()?.string()
                        Log.d("rs",rs)
                        activity?.runOnUiThread {

                            var p=Bundle()
                            p.putString("token",rs)
                            navController.navigate(R.id.github_login,p)
                        }
                    }

                })

            }else{


                initWebView()
                ghlf_login_wv.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        view.loadUrl(url)
                        Log.d("跳转的url",url)
                        var p=Bundle()
                        p.putString("url",url)
                        // val navController = findNavController()//as NavHostFragment
                        if (!url.contains("authorize")){
                            navController.navigate(R.id.github_login,p)
                        }
                       //
                        return true
                    }
                }
                val url = ConstantUtils.GITHUB_OAUTH_URL+"authorize?redirect_uri=$callback&client_id=$client_id&scope=user:mail"
                ghlf_login_wv.loadUrl(url)
                Log.d("url",URLEncoder.encode(url,"utf-8")+"...."+url+"..."+URLDecoder.decode(url,"utf-8"))

                Log.d("return url ",ghlf_login_wv.url)
            }
        }



    }

    private fun initWebView() {

        ghlf_login_wv.settings.userAgentString=newUA
        ghlf_login_wv.settings.cacheMode=WebSettings.LOAD_NO_CACHE
        ghlf_login_wv.clearCache(true)
        ghlf_login_wv.clearHistory()
        ghlf_login_wv.settings.javaScriptEnabled = true;
    }

}

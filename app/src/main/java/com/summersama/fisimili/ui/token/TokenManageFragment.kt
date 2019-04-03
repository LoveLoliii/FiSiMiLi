package com.summersama.fisimili.ui.token

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.summersama.fisimili.R
import kotlinx.android.synthetic.main.token_manage_fragment.*

class TokenManageFragment : Fragment() {

    companion object {
        fun newInstance() = TokenManageFragment()
    }

    private lateinit var viewModel: TokenManageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.token_manage_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TokenManageViewModel::class.java)
        // TODO: Use the ViewModel
        observe()
        viewModel.getSToken(this.context!!)

        tmf_save_btn.setOnClickListener {
            viewModel.setSToken(this.context!!,tmf_token_et.text.toString())
        }

    }

    private fun observe() {
        viewModel.token.observe(this, Observer {
            tmf_token_et.setText(it)
        })

    }

}

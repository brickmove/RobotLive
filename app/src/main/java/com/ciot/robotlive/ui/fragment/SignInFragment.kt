package com.ciot.robotlive.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ciot.robotlive.MainActivity
import com.ciot.robotlive.bean.DealResult
import com.ciot.robotlive.constant.ConstantLogic
import com.ciot.robotlive.databinding.FragmentSignInBinding
import com.ciot.robotlive.ui.base.BaseFragment

class SignInFragment : BaseFragment() {
    companion object {
        private const val TAG = "SignInFragment"
    }
    private lateinit var binding : FragmentSignInBinding
    private lateinit var account : String
    private lateinit var password : String
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.etAccount.setOnClickListener{
            account = binding.etAccount.text.toString()
        }
        binding.etAccount.setOnClickListener{
            password = binding.etPwd.text.toString()
        }
        binding.btSignIn.setOnClickListener{
            if (account.length != ConstantLogic.accountLen || password.length != ConstantLogic.pwdLen) {
                // 弹出字符长度限制：请输入完整账号密码
                return@setOnClickListener
            }
            val dealResult = DealResult()
            (activity as MainActivity).signIn(account, password)
        }
    }
}

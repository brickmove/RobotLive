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
import com.ciot.robotlive.utils.SPUtils

class SignInFragment : BaseFragment() {
    companion object {
        private const val TAG = "SignInFragment"
    }
    private lateinit var binding : FragmentSignInBinding
    private lateinit var account : String
    private lateinit var password : String
    private var mIsChecked : Boolean = false
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.ckAgree.setOnCheckedChangeListener { _, isChecked ->
            mIsChecked = isChecked
        }
        binding.btSignIn.setOnClickListener{
            // 需要先勾选用户协议和隐私政策
            if (!mIsChecked) {
                (activity as MainActivity).showToast("Please check the User Agreement and Privacy Policy")
                return@setOnClickListener
            }

            account = binding.etAccount.text.toString()
            password = binding.etPwd.text.toString()
            // 账号密码有误
//            if (account.length != ConstantLogic.accountLen || password.length != ConstantLogic.pwdLen) {
//                (activity as MainActivity).showToast("The account or password is incorrect.")
//                return@setOnClickListener
//            }

            (activity as MainActivity).signIn(account, password)
        }
    }

    override fun refreshData(isRefreshImmediately: Boolean, data: DealResult) {
        // 返回首页时使用上一次登录的账号和密码
        account = SPUtils.getString(ConstantLogic.SP_SAVE_SIGN_ACCOUNT, "").toString()
        password = SPUtils.getString(ConstantLogic.SP_SAVE_SIGN_PWD, "").toString()
    }
}

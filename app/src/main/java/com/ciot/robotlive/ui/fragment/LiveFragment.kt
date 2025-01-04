package com.ciot.robotlive.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.ciot.robotlive.databinding.FragmentLiveBinding
import com.ciot.robotlive.ui.base.BaseFragment

class LiveFragment : BaseFragment() {
    companion object {
        private const val TAG = "LiveFragment"
    }
    private lateinit var binding : FragmentLiveBinding
    private var wvLive: WebView? = null
    private var tvRemainTime: TextView? = null
    private var btRecharge: Button? = null
    private var ibVoiceInput: ImageButton? = null
    private var ibVoiceOutput: ImageButton? = null
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
        binding = FragmentLiveBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        wvLive = binding.wvLive
        tvRemainTime = binding.tvRemainTime
        btRecharge = binding.btRecharge
        ibVoiceInput = binding.btVoiceInput
        ibVoiceOutput = binding.btVoiceOutput
    }


}

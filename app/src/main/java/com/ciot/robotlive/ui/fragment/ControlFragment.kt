package com.ciot.robotlive.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ciot.robotlive.databinding.FragmentSignInBinding
import com.ciot.robotlive.ui.base.BaseFragment

class ControlFragment : BaseFragment() {
    companion object {
        private const val TAG = "ControlFragment"
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
    }
}

package com.ciot.robotlive

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ciot.robotlive.utils.MyLog

class MainActivity : AppCompatActivity() , View.OnClickListener {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyLog.d(TAG, "MainActivity onCreate start")
    }

    override fun onClick(view: View?) {
        when (view?.id) {

        }
    }
}


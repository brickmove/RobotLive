package com.ciot.robotlive

import com.ciot.robotlive.bean.DealResult
import com.ciot.robotlive.ui.base.BaseFragment
import com.ciot.robotlive.ui.base.BaseView
import java.util.LinkedList

/**
 * Created by p'c on 2024/12/30.
 * Description:
 * Encoding: utf-8
 */
interface MainView : BaseView {

    fun update(results: LinkedList<out DealResult>?)
    fun getCurrentFragment(): BaseFragment?

}
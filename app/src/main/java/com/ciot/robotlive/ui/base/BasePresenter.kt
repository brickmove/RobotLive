package com.ciot.robotlive.ui.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by p'c on 2024/12/30.
 * Description:
 * Encoding: utf-8
 */
abstract class BasePresenter<V> {
    private var mView: V? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    fun attachView(view: V) {//由具体的presenter实现类关联它的View
        this.mView = view
    }

    fun detechView() {
        mView = null
        onUnsubscribe()
    }

    // RXjava取消注册，以避免内存泄露
    private fun onUnsubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.clear()
        }
    }

    fun addSubscription(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let { mCompositeDisposable!!.add(disposable) }
    }

    open fun doSleep() {

    }
}
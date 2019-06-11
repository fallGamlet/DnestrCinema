package com.fallgamlet.dnestrcinema.ui.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fallgamlet.dnestrcinema.R
import com.google.android.material.snackbar.Snackbar

open class BaseFragment: Fragment() {
    private var errorLive: MutableLiveData<Throwable> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setErrorLive(errorLive: MutableLiveData<Throwable>) {
        this.errorLive.removeObservers(this)
        this.errorLive = errorLive
        errorLive.observe(this, Observer { onError(it) } )
    }

    protected fun setLoadingLive(loadingLive: MutableLiveData<Boolean>) {
        loadingLive.observe(this, Observer { onLoading(it) } )
    }


    open protected fun onError(throwable: Throwable?) {
        throwable ?: return

        errorLive.value = null
        showError(throwable)
    }

    open protected fun showError(throwable: Throwable) {
        val view = this.view?: return

        val message = throwable.toString()

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(R.string.label_more) { showErrorInDialog(throwable) }
            .show()
    }

    open protected fun showErrorInDialog(throwable: Throwable) {
        val context = this.context ?: return

        AlertDialog.Builder(context)
            .setMessage(throwable.toString())
            .create()
            .show()
    }


    open protected fun onLoading(value: Boolean?) {

    }

}
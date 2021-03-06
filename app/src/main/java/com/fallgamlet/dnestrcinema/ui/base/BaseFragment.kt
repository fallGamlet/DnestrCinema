package com.fallgamlet.dnestrcinema.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.utils.ViewUtils
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment: Fragment() {
    private var errorLive: MutableLiveData<Throwable?> = MutableLiveData()
    private var loadingLive: MutableLiveData<Boolean?> = MutableLiveData()

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    protected fun setErrorLive(errorLive: MutableLiveData<Throwable?>) {
        this.errorLive.removeObservers(this)
        this.errorLive = errorLive
        errorLive.observe(this, Observer { onError(it) } )
    }

    protected fun setLoadingLive(loadingLive: MutableLiveData<Boolean?>) {
        this.loadingLive.removeObservers(this)
        this.loadingLive = loadingLive
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

        ViewUtils.makeSnackbar(view, message, Snackbar.LENGTH_LONG)
            .setAction(R.string.label_more) { showErrorDetails(throwable) }
            .show()
    }

    open protected fun showErrorDetails(throwable: Throwable) {
        val context = this.context ?: return
        val message = "${throwable.message}\n\n$throwable"

        AlertDialog.Builder(context)
            .setMessage(message)
            .create()
            .show()
    }

    open protected fun onLoading(value: Boolean?) {

    }

    protected fun getViewModelProvider(): ViewModelProvider {
        return activity?.let { ViewModelProviders.of(it) }
            ?: ViewModelProviders.of(this)
    }

}

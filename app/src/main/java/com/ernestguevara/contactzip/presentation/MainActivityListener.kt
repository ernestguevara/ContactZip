package com.ernestguevara.contactzip.presentation

interface MainActivityListener {
    fun setToolbarTitle(title: String)
    fun showLoadingDialog()
    fun dismissLoadingDialog()
}
package com.ernestguevara.contactzip

import androidx.fragment.app.Fragment
import com.ernestguevara.contactzip.presentation.MainActivityListener

open class BaseFragment: Fragment(), MainActivityListener {
    override fun setToolbarTitle(title: String) {
        if (activity is MainActivityListener) {
            (activity as MainActivityListener).setToolbarTitle(title)
        }
    }
}
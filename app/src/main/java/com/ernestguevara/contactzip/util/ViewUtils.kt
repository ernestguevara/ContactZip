package com.ernestguevara.contactzip.util

import android.view.View

/** make view visible */
fun View.makeVisible(){
    this.visibility = View.VISIBLE
}
/** make view gone */
fun View.makeGone(){
    this.visibility = View.GONE
}
/** make view invisible */
fun View.makeInvisible(){
    this.visibility = View.INVISIBLE
}
/** make view visible or gone */
fun View.makeVisibleOrGone(condition:Boolean){
    when (condition){
        true -> this.makeVisible()
        else -> this.makeGone()
    }
}
/** make view visible or invisible */
fun View.makeVisibleOrInvisible(condition:Boolean){
    when(condition){
        true -> this.makeVisible()
        else -> this.makeInvisible()
    }
}

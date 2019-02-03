package com.b5eg.codegeneration

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.b5eg.annotations.BindAction
import com.b5eg.annotations.BindListener


class MainActivity : AppCompatActivity(), Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindFields_Listener_generateMethod(this, "GO_ONE")
        bindFields_Listener_generateMethod(this, "GO_TWO")
    }

    override fun goOne() {
        Log.d(tag, "Hello one world!")
    }

    override fun goTwo() {
        Log.d(tag, "Hello two world!")
    }

    private val tag = MainActivity::class.java.simpleName
}

@BindListener
interface Listener {

    @BindAction(actionName = "GO_ONE")
    fun goOne()

    @BindAction(actionName = "GO_TWO")
    fun goTwo()
}


//@BindListener
//interface Listener2 {
//
//    @BindAction(actionName = "GO_ONE_ONE")
//    fun goOneOne()
//
//    @BindAction(actionName = "GO_TWO_TWO")
//    fun goTwoTwo()
//}

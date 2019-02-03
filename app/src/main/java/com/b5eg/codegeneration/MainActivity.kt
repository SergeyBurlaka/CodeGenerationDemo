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

        bindFields_Listener(this, "GO_ONE")
    }

    override fun goOne() {
        Log.d("TAG1", "Hello world! GO ONE!!")
    }

    override fun goTwo() {
        Log.d("TAG1", "Hello world 2!")
    }
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

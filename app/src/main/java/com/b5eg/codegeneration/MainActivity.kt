package com.b5eg.codegeneration

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.b5eg.annotations.BindAction
import com.b5eg.annotations.BindField
import com.b5eg.annotations.BindListener


class MainActivity : AppCompatActivity(), Listener {

    val billList = listOf(
        Bil("Osama", "4300"),
        Bil("Rao", "1234"),
        Bil("SomeOtherDude", "2434")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.main_recycler_view).run {
            adapter = MainRecyclerViewAdapter(billList)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


    override fun goOne(listener: Listener?) {
        Log.d("TAG1", "Hello world!")
    }

    override fun goTwo(listener: Listener?) {
        Log.d("TAG1", "Hello world 2!")
    }
}

@BindListener
interface Listener {

    @BindAction(actionName = "GO_ONE")
    fun goOne(listener: Listener? = null)

    @BindAction(actionName = "GO_TWO")
    fun goTwo(listener: Listener? = null)
}


class MainRecyclerViewAdapter(private val listOfObjects: List<Bil>) : RecyclerView.Adapter<MainItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        return MainItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listOfObjects.size
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        holder.bind(listOfObjects[position])
    }
}

data class Bil(val name: String, val amount: String)


class MainItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindField(viewIds = ["name", "amount"], viewName = "view")
    fun bind(item: Bil) {

        bindFields(item, itemView)
    }
}
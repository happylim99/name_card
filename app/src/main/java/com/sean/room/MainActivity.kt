package com.sean.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sean.room.databinding.ActivityMainBinding
import com.sean.room.db.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecycleViewAdapter({selectedItem:Subscriber->listItemClicked(selectedItem)})
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscribersList()
    }

    private fun  displaySubscribersList() {
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("TAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
//            binding.subscriberRecyclerView.adapter =
//                MyRecycleViewAdapter(it, {selectedItem:Subscriber->listItemClicked(selectedItem)})
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
//        Toast.makeText(this,
//            "selected name is ${subscriber.name}",
//            Toast.LENGTH_LONG
//        ).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}

package com.sean.room.db

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sean.room.R
import com.sean.room.databinding.ListItemBinding
import com.sean.room.generated.callback.OnClickListener

class MyRecycleViewAdapter(
    private val clickListener: (Subscriber)->Unit
): RecyclerView.Adapter<MyViewHolder>()
{
    private val subscriberList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscriberList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    fun setList(subscribers: List<Subscriber>) {
        subscriberList.clear()
        subscriberList.addAll(subscribers)
    }

}

class MyViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
        binding.textViewName.text = subscriber.name
        binding.textViewEmail.text = subscriber.email
        binding.listItemLayout.setOnClickListener{
            clickListener(subscriber)
        }
    }
}
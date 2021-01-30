package com.sean.room

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import com.sean.room.db.Subscriber
import com.sean.room.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository):
    ViewModel(), Observable {

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val btnSave = MutableLiveData<String>()
    @Bindable
    val btnDelete = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() {
            return statusMessage
        }

    init {
        btnSave.value = "Save"
        btnDelete.value = "Clear All"
    }

    fun saveOrUpdate() {
        if(inputName.value == null) {
            statusMessage.value = Event("Name required")
        } else if(inputEmail.value == null) {
            statusMessage.value = Event("Email required")
        } else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Invalid email")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            } else {
                val name: String = inputName.value!!
                val email: String = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if(isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            deleteAll()
        }
    }

    fun insert(subscriber: Subscriber) {
        viewModelScope.launch {
            val newRowId = repository.insert(subscriber)
            if(newRowId > -1) {
                statusMessage.value = Event("Subscriber inserted successfully $newRowId")
            } else {
                statusMessage.value = Event("Error occurred")
            }
        }
    }

    fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        val rowNum = repository.update(subscriber)
        if(rowNum > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            btnSave.value = "Save"
            btnDelete.value = "Clear All"
            statusMessage.value = Event("Id $rowNum updated successfully")
        } else {
            statusMessage.value = Event("Error occurred")
        }
    }

    fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        val rowNum = repository.delete(subscriber)
        if(rowNum > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            btnSave.value = "Save"
            btnDelete.value = "Clear All"
            statusMessage.value = Event("Id $rowNum deleted successfully")
        } else {
            statusMessage.value = Event("Error occurred")
        }
    }

    fun deleteAll(): Job = viewModelScope.launch {
        val rowNum = repository.deleteAll()
        if(rowNum > 0) {
            statusMessage.value = Event("All Subscribers deleted successfully")
        } else {
            statusMessage.value = Event("Error occurred")
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        btnSave.value = "Update"
        btnDelete.value = "Delete"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}
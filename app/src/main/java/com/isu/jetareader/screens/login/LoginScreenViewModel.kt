package com.isu.jetareader.screens.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.isu.jetareader.model.MUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {
//    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) = viewModelScope.launch{
        try {
            if(_loading.value == false){
                _loading.value = true
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
                    if(task.isSuccessful){
                        home()
                    }else{

                    }
                }
                _loading.value = false
            }
        }catch (ex :Exception){
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String,home : ()-> Unit) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    val displayName = task.result?.user?.email?.split('@')?.get(0)
                    createUser(displayName)
                    if (task.isSuccessful) {
                        home()
                        Log.d(TAG, "signInWithEmailAndPassword: login su")
                    } else {
                        Log.d("FB", "signInWithEmailAndPassword: ${task.result.toString()}")
                    }
                }
        } catch (e: Exception) {
            Log.d("FB", "signInWithEmailAndPassword: ${e.message}")
        }

    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = MUser(userId = userId.toString(),displayName = displayName.toString(), avatarUrl = "", quote = "Life is great", profession = "Android Developer", id = null).toMap()
        FirebaseFirestore.getInstance().collection("users").add(user)
    }
}
package com.isu.jetareader.screens.details

import androidx.lifecycle.ViewModel
import com.isu.jetareader.data.Resource
import com.isu.jetareader.model.Item
import com.isu.jetareader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository):ViewModel() {
    suspend fun getBookInfo(bookId :String) : Resource<Item> {
        return repository.getBookInfo(bookId)
    }
}
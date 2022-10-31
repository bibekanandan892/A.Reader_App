package com.isu.jetareader.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.isu.jetareader.components.InputField
import com.isu.jetareader.components.ReaderAppBar
import com.isu.jetareader.model.Item
import com.isu.jetareader.model.MBook
import com.isu.jetareader.navigation.ReaderScreens

@Composable
fun ReaderBookSearchSearchScreen(
    navController: NavController,
    viewModel: BooksSearchViewModel = hiltViewModel(),
) {
    val listOfBooks = listOf(
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null)
    )

    Scaffold(topBar = {
        ReaderAppBar(title = "Search Books",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false) {
            navController.popBackStack()
        }
    }) {
        Surface() {
            Column {
                SearchForm(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) { searchQuery->
                    viewModel.searchBooks(query = searchQuery)
                }
                Spacer(modifier = Modifier.height(13.dp))
                BookList(navController = navController,viewModel)
            }

        }
    }


}

@Composable
fun BookList(
    navController: NavController,
    viewModel: BooksSearchViewModel = hiltViewModel(),
) {
    val listOfBooks = viewModel.list
    if(viewModel.isLoading){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(20.dp))
    }else{
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)) {
            items(listOfBooks) { book ->
                BookRow(book = book, navController)
            }
        }
    }


}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {},
) {
    Column {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid: Boolean = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }
        InputField(valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid)
                    return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()

            })

    }
}

//@Preview
@Composable
fun BookRow(
    book: Item ,
    navController: NavController,
) {

    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            val imageUrl = if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty()) {
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1317793965i/11468377.jpg"
            } else {
                book.volumeInfo.imageLinks.smallThumbnail
            }
            Image(painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "book Image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp))
            Column(modifier = Modifier.padding(2.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly) {
                Text(text = book.volumeInfo.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(text = "Author ${book.volumeInfo.authors ?: "NA"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)
                Text(text = "Date: ${book.volumeInfo.publishedDate ?: "NA"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)
                Text(text = "${book.volumeInfo.categories ?: "NA"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

            }
        }
    }
}

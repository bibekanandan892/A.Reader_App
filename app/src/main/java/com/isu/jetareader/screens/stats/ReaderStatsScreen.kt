package com.isu.jetareader.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.isu.jetareader.components.ReaderAppBar
import com.isu.jetareader.model.Item
import com.isu.jetareader.model.MBook
import com.isu.jetareader.navigation.ReaderScreens
import com.isu.jetareader.screens.home.HomeScreenViewModel
import java.util.*

@Composable
fun ReaderStatsScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser
    Scaffold(topBar = {
        ReaderAppBar(title = "Book Stats",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false){
            navController.popBackStack()
        }
    }) {
        books = if (!viewModel.data.value.data.isNullOrEmpty()) {
            viewModel.data.value.data!!.filter { mBook ->
                (mBook.userId == currentUser?.uid)
            }
        } else {
            emptyList()
        }
        Column {
            Row {
                Box(modifier = Modifier
                    .size(45.dp)
                    .padding(2.dp)) {
                    Icon(imageVector = Icons.Sharp.Person, contentDescription = "icon")
                }
                Text(text = "Hi, ${currentUser?.email.toString().split("@")[0].uppercase(Locale.ROOT)}")
            }
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), shape = CircleShape,
            elevation = 5.dp) {
                var readBookList : kotlin.collections.List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                    books.filter {mBook ->
                        (mBook.userId == currentUser?.uid)  && (mBook.finishedReading!= null)
                    }
                }else{
                    emptyList()
                }
                val readingBooks = books.filter {mBook ->
                    (mBook.startedReading != null) && mBook.finishedReading == null
                }
                Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.Start) {
                    Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                    Divider()
                    Text(text = "You're reading: ${readingBooks.size} books")
                    Text(text = "You're read: ${readBookList.size} books")

                }
            }
            if(viewModel.data.value.loading == true){
                LinearProgressIndicator()
            }else{
                Divider()
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), contentPadding = PaddingValues(16.dp)){
                    var readBooks : List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()){
                        viewModel.data.value.data!!.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    }else{
                        emptyList()
                    }
                    items(items= readBooks){book->
                        BookRowStats(book = book)
                    }

                }
            }
        }
    }
}


//@Preview
@Composable
fun BookRowStats(
    book: MBook,
) {

    Card(modifier = Modifier
        .clickable {
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            val imageUrl = if (book.photoUrl.toString().isEmpty()) {
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1317793965i/11468377.jpg"
            } else {
                book.photoUrl.toString()
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
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(text = "Author ${book.authors ?: "NA"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)
                Text(text = "Date: ${book.publishedDate ?: "NA"}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)
                Text(text = book.categories ?: "NA",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

            }
        }
    }
}


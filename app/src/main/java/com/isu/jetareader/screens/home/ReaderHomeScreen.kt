package com.isu.jetareader.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.isu.jetareader.components.FABContent
import com.isu.jetareader.components.ListCard
import com.isu.jetareader.components.ReaderAppBar
import com.isu.jetareader.components.TitleSection
import com.isu.jetareader.model.MBook
import com.isu.jetareader.navigation.ReaderScreens

@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        ReaderAppBar(title = "A.Reader", navController = navController)
    },
        floatingActionButton = { FABContent { navController.navigate(ReaderScreens.SearchScreen.name) } }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController = navController, viewModel = viewModel)
        }

    }

}

//@Preview
@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel) {

//    val listOfBooks = listOf(
//        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
//        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
//        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
//        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null),
//        MBook(id = "alksjfd", title = "Hello again", authors = "All of us ", notes = null)
//    )
    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }
    }

    val currentUserName = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrBlank())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else "N/A"
    Column(modifier = Modifier.padding(2.dp), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your reading \n " + "activity write now...")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant)
                Text(text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip)
                Divider()
            }
        }
        ReadingRightNowArea(
            listOfBooks = listOfBooks,
            navController = navController)
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    val addedBooks = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }
    HorizontalScrollableComponent(addedBooks) {
        navController.navigate(ReaderScreens.UpdateScreen.name+"/$it")
    }
}


@Composable
fun ReadingRightNowArea(listOfBooks: List<MBook>, navController: NavController) {
    val readingNowList = listOfBooks.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }
    HorizontalScrollableComponent(readingNowList) {
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPress: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)) {
        for (book in listOfBooks) {
            ListCard(book) {
                onCardPress(book.googleBookId.toString())
            }
        }
    }
}






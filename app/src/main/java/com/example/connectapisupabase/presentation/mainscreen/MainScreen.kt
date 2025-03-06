package com.example.connectapisupabase.presentation.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.connectapisupabase.domain.state.ResultState


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {

    val result = viewModel.resultState.collectAsState()
    val books = viewModel.books.collectAsState()

    var email = remember { mutableStateOf("test@test.ru") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Email")
        Spacer(Modifier.height(10.dp))
        TextField(email.value, { email.value = it })
        Button(
            onClick = { viewModel.signUp(email.value) }
        ) {
            Text("Зарегистрироваться")
        }
        Button(
            onClick = { viewModel.signIn(email.value) }
        ) {
            Text("Войти")
        }
        Button(
            onClick = { viewModel.getBooks() }
        ) {
            Text("Вывести список")
        }
        when (result.value) {
            is ResultState.Error -> {
                Text(text = (result.value as ResultState.Error).message)
            }

            ResultState.Initialized -> {

            }

            ResultState.Loading -> {
                Box {
                    CircularProgressIndicator()
                }
            }

            is ResultState.Success -> {
                Text(text = (result.value as ResultState.Success).message)
                LazyColumn(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(books.value)
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp)
                        ) {
                            val imageState = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(viewModel.getImage(it))
                                    .size(Size.ORIGINAL).build()
                            ).state
                            if (imageState is AsyncImagePainter.State.Loading) {

                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            if (imageState is AsyncImagePainter.State.Success) {
                                Image(

                                    painter = imageState.painter,
                                    contentDescription = it.title,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(it.title)

                            }
                        }
                    }
                }
            }

        }
    }
}
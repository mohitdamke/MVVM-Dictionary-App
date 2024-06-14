package com.example.mvvmnotesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvvmnotesapp.domain.model.Meaning
import com.example.mvvmnotesapp.domain.model.WordItem
import com.example.mvvmnotesapp.presentation.MainState
import com.example.mvvmnotesapp.presentation.MainUiEvents
import com.example.mvvmnotesapp.presentation.MainViewModel
import com.example.mvvmnotesapp.ui.theme.MVVMNotesAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMNotesAppTheme {
                BarColor()
                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainState by mainViewModel.mainState.collectAsState()
                Scaffold(
                    modifier = Modifier.fillMaxSize().padding(top = 40.dp),
                    topBar = {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            value = mainState.searchWord,
                            onValueChange = {
                                mainViewModel.onEvent(
                                    MainUiEvents.OnSearchWordChange(it)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            mainViewModel.onEvent(
                                                MainUiEvents.OnSearchClick
                                            )
                                        }
                                )
                            },
                            label = {
                                Text(
                                    text = "Search a word",
                                    fontSize = 15.sp,
                                    modifier = Modifier.alpha(0.7f)
                                )
                            },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 19.5.sp
                            )
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        MainScreen(mainState)
                    }

                }

            }
        }
    }

    @Composable
    fun MainScreen(
        mainState: MainState
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 30.dp)
            ) {
                mainState.wordItem?.let { wordItem ->

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = wordItem.word,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = wordItem.phonetic,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 110.dp)
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp
                        )
                    )
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(0.7f)
                    )
            ) {
                if (mainState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    mainState.wordItem?.let { wordItem ->
                        WordResult(wordItem)
                    }
                }
            }
        }
    }

    @Composable
    fun WordResult(wordItem: WordItem) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 32.dp)
        ) {
            items(wordItem.meanings.size) { index ->
                Meaning(
                    meaning = wordItem.meanings[index],
                    index = index
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    @Composable
    fun Meaning(
        meaning: Meaning,
        index: Int
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "${index + 1}. ${meaning.partOfSpeech}",
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(0.4f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(
                        top = 2.dp, bottom = 4.dp,
                        start = 12.dp, end = 12.dp
                    )
            )

            if (meaning.definitions.definition.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {

                    Text(
                        text = "Definition",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = meaning.definitions.definition,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }

            if (meaning.definitions.example.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {

                    Text(
                        text = "Definitions",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = meaning.definitions.example,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }
        }
    }

    @Composable
    private fun BarColor() {
        val systemUiController = rememberSystemUiController()
        val color = MaterialTheme.colorScheme.background
        LaunchedEffect(color) {
            systemUiController.setStatusBarColor(color)

        }
    }
}

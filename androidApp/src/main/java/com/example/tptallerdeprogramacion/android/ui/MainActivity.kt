package com.example.tptallerdeprogramacion.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tptallerdeprogramacion.Greeting
import com.example.tptallerdeprogramacion.android.MyApplicationTheme
import com.example.tptallerdeprogramacion.android.ui.viewModels.PokedexViewModel
import com.example.tptallerdeprogramacion.android.ui.viewModels.factories.PokedexViewModelFactory
import com.example.tptallerdeprogramacion.android.ui.viewModels.screenStates.PokedexScreenState
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.repositories.PokedexRepository
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PokedexViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, PokedexViewModelFactory())[PokedexViewModel::class.java]

        var pokemon = ""
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.screenState.collect {
                    pokemon = when (it) {
                        PokedexScreenState.Loading -> "cargandooo"
                        PokedexScreenState.Error -> "error"
                        is PokedexScreenState.ShowPokedex -> it.pokedex[0].name
                    }
                }
            }
        }
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingView(pokemon)
                }
            }

        }
    }
}

@Composable
fun GreetingView(texto: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        var pokemon by remember { mutableStateOf("") }

        pokemon = texto

        if (pokemon.isNotEmpty()) {
            Text(
                text = "Hello, $pokemon!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
    }
}

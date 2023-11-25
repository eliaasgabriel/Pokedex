package com.example.tptallerdeprogramacion.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.tptallerdeprogramacion.DatabaseDriverFactory
import com.example.tptallerdeprogramacion.android.MyApplicationTheme
import com.example.tptallerdeprogramacion.android.ui.viewModels.PokedexViewModel
import com.example.tptallerdeprogramacion.android.ui.viewModels.factories.PokedexViewModelFactory
import com.example.tptallerdeprogramacion.android.ui.viewModels.screenStates.PokedexScreenState
import com.example.tptallerdeprogramacion.domain.ImageBuilder
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.repositories.PokedexDBRepository
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PokedexViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, PokedexViewModelFactory())[PokedexViewModel::class.java]


        val pokedexDBRepository = PokedexDBRepository(DatabaseDriverFactory(this))

        setContent {
            MyApplicationTheme {

                ShowView(viewModel, pokedexDBRepository)
            }
        }
    }
}

@Composable
fun ShowView(
    pokedexViewModel: PokedexViewModel,
    pokedexDBRepository: PokedexDBRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var pokedex by remember { mutableStateOf(mutableListOf<Pokemon>()) }
    var loading by remember { mutableStateOf("") }

    val getView: () -> Unit = {
        coroutineScope.launch {

            pokedexViewModel.loadPokemons(pokedexDBRepository)
            pokedexViewModel.screenState.collect() {
                when (it) {
                    PokedexScreenState.Loading -> loading = "loading"
                    PokedexScreenState.Error -> loading = "error"
                    is PokedexScreenState.ShowPokedex -> {
                        pokedex = it.pokedex
                        loading = "success"
                    }
                }
            }
        }
    }

    if (loading == "loading") {
        ShowProgressBar()
    } else if (loading == "error") {
        ShowError(getView)
    } else if (loading == "success") {
        PokedexRecycler(pokedex)

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = getView) {
                Text("Ver Pokedex")
            }
        }
    }


}

@Composable
fun ShowProgressBar() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
fun ShowError(getView: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .height(120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Oh no ocurrio un error inesperado :(",
            color = MaterialTheme.colors.primaryVariant,
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = "Verifique si tiene conexion a internet y vuelva a intentarlo.",
            color = MaterialTheme.colors.primaryVariant
        )

        Button(onClick = getView) {
            Text("Ver Pokedex")
        }
    }
}

@Composable
fun PokedexRecycler(pokemons: MutableList<Pokemon>) {

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {

        var count = 0
        val pokemonsDouble = mutableListOf<Pokemon>()

        items(items = pokemons) { pokemon ->
            if (count < 2) {
                pokemonsDouble.add(pokemon)
                count++
            } else {
                ListPokemon(pokemonsDouble)
                pokemonsDouble.clear()
                count = 0
            }
        }

    }

}

@Composable
fun ListPokemon(pokemons: List<Pokemon>) {

    Row {
        for (pokemon in pokemons) {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .height(140.dp)
            ) {
                Surface(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 5.dp)
                        .fillMaxSize(),
                    shape = RoundedCornerShape(15)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {

                            AsyncImage(
                                model = pokemon.url?.let { ImageBuilder.buildPokemonImageByUrl(it) },
                                contentDescription = "PokemonImage",
                                modifier = Modifier.size(90.dp)
                            )
                        }
                        Row {
                            Text(
                                text = pokemon.name.capitalizeFirstLetter(),
                                style = MaterialTheme.typography.h6.copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                        }
                    }
                }
            }
        }

    }
}

fun String.capitalizeFirstLetter(): String {
    return if (isNotEmpty()) {
        this[0].uppercaseChar() + substring(1)
    } else {
        this
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        val pokemons = mutableListOf<Pokemon>()
        val pokemon = Pokemon("pikachu", "")
        for (i in 1..5) {
            pokemons.add(pokemon)
        }

        PokedexRecycler(pokemons)
    }

}

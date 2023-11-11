package com.example.tptallerdeprogramacion.android.ui

import android.os.Bundle
import android.widget.AbsListView.RecyclerListener
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Observer
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.tptallerdeprogramacion.DatabaseDriverFactory
import com.example.tptallerdeprogramacion.Greeting
import com.example.tptallerdeprogramacion.android.MyApplicationTheme
import com.example.tptallerdeprogramacion.android.ui.viewModels.PokedexViewModel
import com.example.tptallerdeprogramacion.android.ui.viewModels.factories.PokedexViewModelFactory
import com.example.tptallerdeprogramacion.android.ui.viewModels.screenStates.PokedexScreenState
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.PokemonImage
import com.example.tptallerdeprogramacion.domain.repositories.PokedexDBRepository
import com.example.tptallerdeprogramacion.domain.repositories.PokedexRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PokedexViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, PokedexViewModelFactory())[PokedexViewModel::class.java]


        val pokedexDBRepository = PokedexDBRepository(DatabaseDriverFactory(this))

        viewModel.cargarPokemons(pokedexDBRepository)

        viewModel.pokedex.observe(this, Observer { pokedexResponse ->
            pokedexResponse.results?.let { viewModel.cargarImagenes(it, pokedexDBRepository) }

            viewModel.pokeUrlImages.observe(this, Observer { pokeUrlImages ->
                setContent{
                    MyApplicationTheme {
                        pokedexResponse.results?.let { PokedexRecycler(it, pokeUrlImages) }
                    }
                }
            })

        })
    }
}

@Composable
fun PokedexRecycler(pokemons : List<Pokemon>, imagesPokemon : List<PokemonImage>){
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){

        items(items = pokemons){
                pokemon ->
            var pokeImageUrl = ""
            for(pokeImage in imagesPokemon){
                if(pokeImage.url == pokemon.url){
                    pokeImageUrl = pokeImage.image
                }
            }
            ListPokemon(pokemon = pokemon, pokeImageUrl)

        }

    }

}

@Composable
fun ListPokemon(pokemon: Pokemon, pokeImage : String){
    Surface(color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .height(120.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    AsyncImage(
                        model = pokeImage,
                        contentDescription = "PokemonImage",
                        modifier = Modifier.size(90.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    Text(text = pokemon.name.capitalizeFirstLetter(), style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.ExtraBold
                    ))
                }

                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = "Capturar")
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
        var pokemons = mutableListOf<Pokemon>()
        var pokeImages = mutableListOf<PokemonImage>()
        var pokemon = Pokemon("pikachu", "")
        for(i in 1..5){
            pokemons.add(pokemon)
        }

        PokedexRecycler(pokemons, pokeImages)
    }

}

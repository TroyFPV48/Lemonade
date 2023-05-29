package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lemonade.ui.theme.LemonadeTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private var lemonadeState = mutableStateOf("select")
    private var lemonSize = mutableStateOf(-1)
    private var squeezeCount = mutableStateOf(-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                LemonadeApp(
                    lemonadeState = lemonadeState.value,
                    lemonSize = lemonSize.value,
                    squeezeCount = squeezeCount.value,
                    onLemonTreeClick = { clickLemonTree() },
                    onLemonClick = { clickLemon() },
                    onGlassClick = { clickGlass() },
                    onEmptyGlassClick = { clickEmptyGlass() }
                )
            }
        }

        if (savedInstanceState != null) {
            lemonadeState.value = savedInstanceState.getString(LEMONADE_STATE, "select") ?: "select"
            lemonSize.value = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount.value = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState.value)
        outState.putInt(LEMON_SIZE, lemonSize.value)
        outState.putInt(SQUEEZE_COUNT, squeezeCount.value)
        super.onSaveInstanceState(outState)
    }

    private fun clickLemonTree() {
        when (lemonadeState.value) {
            SELECT -> {
                lemonSize.value = -1
                squeezeCount.value = -1
                lemonadeState.value = SQUEEZE
            }
            RESTART -> {
                lemonSize.value = -1
                squeezeCount.value = -1
                lemonadeState.value = SELECT
            }
        }
    }

    private fun clickLemon() {
        when (lemonadeState.value) {
            SQUEEZE -> {
                lemonSize.value = generateRandomLemonSize()
                squeezeCount.value = generateRandomSqueezeCount()
                lemonadeState.value = DRINK
            }
        }
    }

    private fun clickGlass() {
        when (lemonadeState.value) {
            DRINK -> {
                lemonSize.value = -1
                squeezeCount.value = -1
                lemonadeState.value = RESTART
            }
        }
    }

    private fun clickEmptyGlass() {
        when (lemonadeState.value) {
            RESTART -> {
                lemonSize.value = -1
                squeezeCount.value = -1
                lemonadeState.value = SELECT
            }
        }
    }

    private fun generateRandomLemonSize(): Int {
        return Random.nextInt(2, 5)
    }

    private fun generateRandomSqueezeCount(): Int {
        return Random.nextInt(2, 5)
    }

    companion object {
        private const val LEMONADE_STATE = "LEMONADE_STATE"
        private const val LEMON_SIZE = "LEMON_SIZE"
        private const val SQUEEZE_COUNT = "SQUEEZE_COUNT"
        private const val SELECT = "select"
        private const val SQUEEZE = "squeeze"
        private const val DRINK = "drink"
        private const val RESTART = "restart"
    }
}

@Composable
fun LemonadeApp(
    lemonadeState: String,
    lemonSize: Int,
    squeezeCount: Int,
    onLemonTreeClick: () -> Unit,
    onLemonClick: () -> Unit,
    onGlassClick: () -> Unit,
    onEmptyGlassClick: () -> Unit
) {
    val SELECT = "select"
    val SQUEEZE = "squeeze"
    val DRINK = "drink"
    val RESTART = "restart"

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LemonTree(lemonadeState, Modifier.clickable { onLemonTreeClick() })

        Spacer(modifier = Modifier.height(16.dp))



        when (lemonadeState) {
            SELECT -> Text(text = "Tap the lemon tree to select a lemon")
            SQUEEZE -> Text(text = "Tap the lemon to squeeze it")
            DRINK -> Text(text = "Tap the glass to drink the lemonade")
            RESTART -> Text(text = "Tap the empty glass to start again")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (lemonadeState == SQUEEZE) {
            Lemon(lemonSize, Modifier.clickable { onLemonClick() })
        }

        if (lemonadeState == DRINK) {
            Glass(lemonSize, squeezeCount, Modifier.clickable { onGlassClick() })
        }

        if (lemonadeState == RESTART) {
            EmptyGlass(Modifier.clickable { onEmptyGlassClick() })
        }
    }
}

@Composable
fun LemonTree(lemonadeState: String, modifier: Modifier = Modifier) {
    val SELECT = "select"
    val RESTART = "restart"
    val lemonTreeImageRes = when (lemonadeState) {
        SELECT, RESTART -> R.drawable.lemon_tree
        else -> R.drawable.lemon_tree
    }

    Image(
        painter = painterResource(lemonTreeImageRes),
        contentDescription = "Lemon Tree",
        modifier = modifier
    )
}

@Composable
fun Lemon(lemonSize: Int, modifier: Modifier = Modifier) {
    val lemonImageRes = when (lemonSize) {
        2 -> R.drawable.lemon_squeeze
        3 -> R.drawable.lemon_squeeze
        4 -> R.drawable.lemon_squeeze
        else -> R.drawable.lemon_squeeze
    }

    Image(
        painter = painterResource(lemonImageRes),
        contentDescription = "Lemon",
        modifier = Modifier.size(100.dp).then(modifier)
    )
}

@Composable
fun Glass(lemonSize: Int, squeezeCount: Int, modifier: Modifier = Modifier) {
    val glassImageRes = when {
        lemonSize == 2 && squeezeCount == 2 -> R.drawable.lemon_drink
        lemonSize == 3 && squeezeCount == 3 -> R.drawable.lemon_drink
        lemonSize == 4 && squeezeCount == 4 -> R.drawable.lemon_drink
        else -> R.drawable.lemon_drink
    }

    Image(
        painter = painterResource(glassImageRes),
        contentDescription = "Glass of Lemonade",
        modifier = Modifier.size(120.dp).then(modifier)
    )
}

@Composable
fun EmptyGlass(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.lemon_restart),
        contentDescription = "Empty Glass",
        modifier = Modifier.size(120.dp).then(modifier)
    )
}

@Preview(showBackground = true)
@Composable
fun LemonadeAppPreview() {
    LemonadeTheme {
        LemonadeApp(
            lemonadeState = "select",
            lemonSize = -1,
            squeezeCount = -1,
            onLemonTreeClick = {},
            onLemonClick = {},
            onGlassClick = {},
            onEmptyGlassClick = {}
        )
    }
}

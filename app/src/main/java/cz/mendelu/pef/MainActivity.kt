package cz.mendelu.pef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.mendelu.pef.navigation.Destination
import cz.mendelu.pef.navigation.NavGraph
import cz.mendelu.pef.ui.screens.TaskListScreen
import cz.mendelu.pef.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                NavGraph(startDestination = Destination.TaskListScreen.route)
                //some change
            }
        }
    }
}

@Composable
fun HelloScreen() {
    var value by rememberSaveable {
        mutableStateOf("")
    }
        HelloScreenContent(value = value, onValueChange = {
            value = it
        })
    }

@Composable
fun HelloScreenContent(value: String, onValueChange: (String) -> Unit){

    Column {
        Text(text = "Test text ")

        OutlinedTextField(value = value, onValueChange = onValueChange)

    }
}
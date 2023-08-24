package com.example.brainstormapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.brainstormapp.ui.note_details.NoteDetailsScreen
import com.example.brainstormapp.ui.note_details.NoteDetailsViewModel
import com.example.brainstormapp.ui.notes_list.NotesListScreen
import com.example.brainstormapp.ui.notes_list.NotesListViewModel
import com.example.brainstormapp.ui.theme.LightPeach
import com.example.brainstormapp.util.NoteDetailEvent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        

        setContent {
//            BrainStormAppTheme {}

            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = LightPeach
                )
            }

            val viewModel = getViewModel<NotesListViewModel>()
            val stateHome = viewModel.state.collectAsState()
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "notesList"){
                composable(route = "notesList") {
                    NotesListScreen(
                    state = stateHome,
                    onEvent = viewModel::onEvent,
                    onNavigateToDetails = {navController.navigate("noteDetails/$it")})}
                composable(
                    route = "noteDetails/{noteId}",
                arguments = listOf(
                    navArgument(name = "noteId"){
                        type = NavType.LongType
                        defaultValue = -1L

                    }
                )
                ){backStackEntry ->
                    val viewModelDetails = getViewModel<NoteDetailsViewModel>()
                    val stateDetails = viewModelDetails.state.collectAsState()
                    val stateBottomSheet = viewModelDetails.bottomSheetState.collectAsState()

                    NoteDetailsScreen(
                        state = stateDetails,
                        bottomSheetView = stateBottomSheet,
                        onNavigateUp = {
                            viewModelDetails.onEvent(NoteDetailEvent.UpsertNote)
                            navController.popBackStack()
                        },
                        onEvent = {viewModelDetails.onEvent(it)})
                }
            }



        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BrainStormAppTheme {
//        Greeting("Android", viewModel)
//    }
//}
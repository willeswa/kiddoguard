package apps.monkpad.kiddoguard.presenter.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import apps.monkpad.kiddoguard.R
import apps.monkpad.kiddoguard.models.AppInfo
import apps.monkpad.kiddoguard.presenter.ui.components.FluidShapeIcon
import apps.monkpad.kiddoguard.presenter.ui.theme.Peach
import apps.monkpad.kiddoguard.presenter.ui.theme.SkyBlue
import apps.monkpad.kiddoguard.presenter.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onAppClick: (AppInfo) -> Unit, // Callback to handle app clicks
    onNavigateToAppSelection: () -> Unit,
    onExitBabyMode: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var showOptions by remember { mutableStateOf(false) } // State to toggle options visibility
    var showMathChallenge by remember { mutableStateOf(false) } // State to show math challenge
    var answer by remember { mutableStateOf("") } // State to store the user's answer
    val apps by viewModel.selectedApps.collectAsState()
    val scope = rememberCoroutineScope()

    // Generate a simple math challenge
    val num1 = remember { (1..9).random() }
    val num2 = remember { (1..9).random() }
    val correctAnswer = num1 + num2

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper), // Reference your wallpaper here
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // This ensures the image fills the entire screen
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 90.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(apps.size) { index ->
                val app = apps[index]
                app.icon?.let {
                    FluidShapeIcon(
                        appIcon = it,
                        index = index,
                        onClick = { onAppClick(app) },
                        isRemovable = showOptions,
                        isDancing = showOptions, // Only dance when options are shown
                        onRemoveClick = {
                            scope.launch {
                                viewModel.deselectApp(app)
                            }
                        }
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                if (apps.isEmpty() && !showOptions) {
                    // Message bubble guiding the user
                    Box(
                        modifier = Modifier
                            .background(
                                color = SkyBlue.copy(0.9f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Tap the settings icon to add apps to baby mode.",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Indicator pointing to the settings icon
                    Box(
                        modifier = Modifier
                            .size(20.dp, 10.dp)
                            .background(
                                color = SkyBlue.copy(0.9f),
                                shape = GenericShape { size, _ ->
                                    moveTo(0f, 0f)
                                    lineTo(size.width / 2, size.height)
                                    lineTo(size.width, 0f)
                                    close()
                                }
                            )
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Settings or Close Icon based on `showOptions` state
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = if (showOptions) Color.White else SkyBlue,
                            shape = CircleShape
                        )
                        .clickable {
                            if (showOptions) showOptions = false else showMathChallenge = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (showOptions) Icons.Default.Close else Icons.Default.Settings,
                        contentDescription = if (showOptions) "Close Options" else "Settings",
                        tint = if (showOptions) SkyBlue else Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Display math challenge dialog
                if (showMathChallenge) {
                    AlertDialog(
                        onDismissRequest = {
                            showMathChallenge = false
                        },
                        title = { Text(text = "Parental Check") },
                        text = {
                            Column {
                                Text(text = "What is $num1 + $num2?")
                                TextField(
                                    value = answer,
                                    onValueChange = { answer = it },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Number
                                    ),
                                    singleLine = true,
                                    trailingIcon = {
                                        if (answer.isNotEmpty()) {
                                            IconButton(onClick = { answer = "" }) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = null,
                                                    tint = Color.Gray
                                                )
                                            }
                                        }
                                    },
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                if (answer.toIntOrNull() == correctAnswer) {
                                    showMathChallenge = false
                                    showOptions = true // Show options and start dancing
                                } else {
                                    answer = "" // Reset the answer if incorrect
                                }
                            }) {
                                Text("Submit")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                showMathChallenge = false
                                answer = ""
                            }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                // Display options when gear is clicked and math challenge is passed
                if (showOptions) {
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Add App Option
                        Button(
                            onClick = { onNavigateToAppSelection() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkyBlue,
                                contentColor = Color.White
                            ),
                            shape = CircleShape
                        ) {
                            Text(text = "Add App", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        // Exit Baby Mode Option
                        Button(
                            onClick = { onExitBabyMode() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Peach,
                                contentColor = Color.White
                            ),
                            shape = CircleShape
                        ) {
                            Text(text = "Exit Baby Mode", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


package apps.monkpad.kiddoguard.presenter.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import apps.monkpad.kiddoguard.models.AppInfo
import apps.monkpad.kiddoguard.presenter.ui.components.AppListItem
import apps.monkpad.kiddoguard.presenter.ui.components.BabyPulsatingScreen
import apps.monkpad.kiddoguard.presenter.ui.components.NoAppsFoundMessage
import apps.monkpad.kiddoguard.presenter.ui.theme.LightGray
import apps.monkpad.kiddoguard.presenter.ui.theme.Peach
import apps.monkpad.kiddoguard.presenter.ui.theme.SkyBlue
import apps.monkpad.kiddoguard.presenter.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val allApps by viewModel.availableApps.collectAsState()
    val selectedApps by viewModel.selectedApps.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    // Start loading the apps when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadAvailableApps()
    }

    Box(modifier = Modifier.fillMaxSize().background(

        color = Color.White
    )) {
        if (loading) {
            // Show an interactive, kid-friendly loading screen while apps are being loaded
            BabyPulsatingScreen()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .background(

                        color = Color.White
                    )
            ) {
                // Row containing Back Button and Search Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Back Button with Circular Background
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = SkyBlue, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Search Bar
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        placeholder = { Text(text = "Search apps", color = SkyBlue) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent, shape = CircleShape),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = SkyBlue,
                            containerColor = SkyBlue.copy(alpha = 0.2f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { /* Handle search action */ }),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SkyBlue)
                        },
                        shape = CircleShape
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filtered lists based on search query
                val filteredSelectedApps = selectedApps.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
                val filteredUnselectedApps = allApps.filter { app ->
                    app.name.contains(searchQuery, ignoreCase = true) && !selectedApps.any { it.packageName == app.packageName }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    // Show "Selected Apps" group only if there are selected apps
                    if (filteredSelectedApps.isNotEmpty()) {
                        item {
                            Text(
                                text = "Selected Apps",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(filteredSelectedApps.size) { index ->
                            val app = filteredSelectedApps[index]
                            val alpha = calculateItemAlpha(index, listState, itemHeight = 72.dp)
                            app.icon?.let {
                                AppListItem(
                                    appName = app.name,
                                    appIcon = it,
                                    isAdded = true,
                                    modifier = Modifier.graphicsLayer(alpha = alpha),
                                    onAddOrRemoveClick = {
                                        scope.launch {
                                            viewModel.deselectApp(app)
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Show "Unselected Apps" group only if there are unselected apps
                    if (filteredUnselectedApps.isNotEmpty()) {
                        item {
                            Text(
                                text = "Unselected Apps",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(filteredUnselectedApps.size) { index ->
                            val app = filteredUnselectedApps[index]
                            val alpha = calculateItemAlpha(index, listState, itemHeight = 72.dp)
                            app.icon?.let {
                                AppListItem(
                                    appName = app.name,
                                    appIcon = it,
                                    isAdded = false,
                                    modifier = Modifier.graphicsLayer(alpha = alpha),
                                    onAddOrRemoveClick = {
                                        scope.launch {
                                            viewModel.selectApp(app)
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Show a message if no apps match the search query
                    if (filteredSelectedApps.isEmpty() && filteredUnselectedApps.isEmpty()) {
                        item {
                            NoAppsFoundMessage(searchQuery)
                        }
                    }
                }
            }
        }

        // Floating Done Button
        AnimatedVisibility(
            visible = !loading,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = { navController.popBackStack() }, // Navigate back to the home screen
                containerColor = SkyBlue,
                contentColor = Color.White,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Done")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Done", fontSize = 16.sp)
                }
            }
        }
    }
}

// Function to calculate the alpha value for an item based on its position and height
@Composable
fun calculateItemAlpha(index: Int, listState: LazyListState, itemHeight: Dp): Float {
    val density = LocalDensity.current
    val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
    val itemInfo = visibleItemsInfo.find { it.index == index } ?: return 1f

    val viewportHeight = listState.layoutInfo.viewportEndOffset
    val itemBottom = itemInfo.offset + with(density) { itemHeight.toPx() }.toInt()

    return if (itemBottom > viewportHeight) {
        val distanceOutOfView = itemBottom - viewportHeight
        1f - (distanceOutOfView / with(density) { itemHeight.toPx() })
    } else {
        1f
    }
}





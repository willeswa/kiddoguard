package apps.monkpad.kiddoguard.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import apps.monkpad.kiddoguard.models.AppInfo
import apps.monkpad.kiddoguard.ui.components.AppListItem
import apps.monkpad.kiddoguard.ui.theme.LightGray
import apps.monkpad.kiddoguard.ui.theme.Peach
import apps.monkpad.kiddoguard.ui.theme.SkyBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(
    allApps: List<AppInfo>,
    selectedApps: Set<String>,
    onAppSelected: (String) -> Unit,
    onAppDeselected: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    navController: NavController // Pass NavController for navigation
) {
    var searchQuery by remember { mutableStateOf("") }

    // State to track FAB visibility
    var fabVisible by remember { mutableStateOf(true) }
    // State to remember the last scroll position
    val listState = rememberLazyListState()
    var lastScrollOffset by remember { mutableStateOf(0) }

    // Group apps into selected and unselected
    val selectedAppList = allApps.filter { selectedApps.contains(it.packageName) }
    val unselectedAppList = allApps.filter { !selectedApps.contains(it.packageName) }

    // Detect scroll direction and update FAB visibility
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { scrollOffset ->
                val currentVisibleIndex = listState.firstVisibleItemIndex

                if (scrollOffset > lastScrollOffset) {
                    fabVisible = false // Scrolling down, hide the FAB
                } else if (scrollOffset < lastScrollOffset) {
                    fabVisible = true // Scrolling up, show the FAB
                }

                lastScrollOffset = scrollOffset // Update the last scroll offset
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(
                    shape = CircleShape,
                    color = LightGray.copy(0.1f)
                )  // Background color for the screen
        ) {
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQueryChanged(it)
                },
                label = { Text(text = "Search apps", color = SkyBlue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = SkyBlue.copy(alpha = 0.2f), shape = CircleShape),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Peach,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = CircleShape,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { /* Handle search action */ })
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List of Apps with checkboxes and icons, grouped into selected and unselected
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                // Section for Selected Apps
                item {
                    Text(
                        text = "Selected Apps",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(selectedAppList.size) { index ->
                    val app = selectedAppList[index]
                    AppListItem(
                        appName = app.name,
                        appIcon = app.icon,
                        isAdded = true,
                        onAddOrRemoveClick = {
                            onAppDeselected(app.packageName)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Section for Unselected Apps
                item {
                    Text(
                        text = "Unselected Apps",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(unselectedAppList.size) { index ->
                    val app = unselectedAppList[index]
                    AppListItem(
                        appName = app.name,
                        appIcon = app.icon,
                        isAdded = false,
                        onAddOrRemoveClick = {
                            onAppSelected(app.packageName)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Floating Done Button
        AnimatedVisibility(
            visible = fabVisible,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
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

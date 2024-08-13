package apps.monkpad.kiddoguard.presenter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultLauncherBottomSheetContent(
    onDismiss: () -> Unit,
    onSetAsDefaultLauncher: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Set KiddoGuard as Default Launcher", style = MaterialTheme.typography.h6)
        Text(text = "Would you like to set KiddoGuard as your default home screen launcher?")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSetAsDefaultLauncher() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Set as Default")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { onDismiss() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Not Now")
        }
    }
}

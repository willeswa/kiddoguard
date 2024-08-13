package apps.monkpad.kiddoguard.presenter.ui.components

import android.app.WallpaperManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun WallpaperBackground(modifier: Modifier = Modifier){
    val context = LocalContext.current
    val wallPaper =  WallpaperManager.getInstance(context)
    val wallpaperDrawable = wallPaper.drawable

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Transparent)
                )
            )
    ){

    }
}
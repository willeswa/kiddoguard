package apps.monkpad.kiddoguard.presenter.ui.components

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apps.monkpad.kiddoguard.presenter.ui.theme.SkyBlue
import apps.monkpad.kiddoguard.presenter.ui.theme.Peach
import apps.monkpad.kiddoguard.R
import apps.monkpad.kiddoguard.utils.drawableToImageBitmap

@Composable
fun AppListItem(
    appName: String,
    appIcon:  Drawable,
    isAdded: Boolean,
    onAddOrRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable{onAddOrRemoveClick()}
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .border(
                width = 2.dp,
                color = if (isAdded) Peach else SkyBlue,
                shape = CircleShape
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // App Icon
        Image(
            bitmap = drawableToImageBitmap(appIcon),
            contentDescription = null,
            modifier = modifier.size(48.dp),  // Icon size
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = modifier.width(16.dp))

        // App Name
        Text(
            text = appName,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = modifier.weight(1f)
        )


            Icon(
                imageVector = if(isAdded) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isAdded) "Added" else "Add",
                tint = if (isAdded) Peach else SkyBlue,
                modifier = Modifier.size(24.dp)
            )

    }
}



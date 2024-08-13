package apps.monkpad.kiddoguard.presenter.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import apps.monkpad.kiddoguard.presenter.ui.theme.LightGray
import apps.monkpad.kiddoguard.presenter.ui.theme.Peach
import apps.monkpad.kiddoguard.presenter.ui.theme.SkyBlue
import apps.monkpad.kiddoguard.utils.drawableToImageBitmap
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FluidShapeIcon(
    appIcon: Drawable,
    index: Int,
    onClick: () -> Unit,
    isRemovable: Boolean, // Determines if the minus icon should be shown
    isDancing: Boolean, // Controls whether the icon should dance
    onRemoveClick: () -> Unit // Callback for clicking the minus icon
) {
    val shapes = listOf(
        roundedSquareShape(),
        roundedHexagonShape()
    )
    val selectedShape = shapes[index % shapes.size]

    val gradient = Brush.linearGradient(
        colors = listOf(
            Peach,
            LightGray,
            SkyBlue
        )
    )

    // Variables for controlling the animation
    val scale: Float
    val rotation: Float

    if (isDancing) {
        // Start animation when `isDancing` is true
        val dance = rememberInfiniteTransition(label = "")
        scale = dance.animateFloat(
            initialValue = 0.9f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 300, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        ).value

        rotation = dance.animateFloat(
            initialValue = -5f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 300, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        ).value
    } else {
        // No animation when `isDancing` is false
        scale = 1f
        rotation = 0f
    }

    Box(modifier = Modifier.size(90.dp), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .size(if (isDancing) 80.dp else 90.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation
                )
                .clickable { onClick() },
            shape = selectedShape,
            shadowElevation = 4.dp,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(brush = gradient)
                    .border(6.dp, Color.White, selectedShape)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = drawableToImageBitmap(appIcon),
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        // Display the minus icon for removal outside the icon
        if (isRemovable) {
            Box(
                modifier = Modifier
                    .size(32.dp) // Slightly larger background
                    .offset(x = (-38).dp, y = (-30).dp) // Position outside the app icon
                    .background(
                        color = Color.Red.copy(alpha = 0.85f), // Softer red with some transparency
                        shape = CircleShape // Circular background for the delete icon
                    )
                    .clickable { onRemoveClick() }, // Clickable area
                contentAlignment = Alignment.Center // Center the icon within the background
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Remove App",
                    tint = Color.White, // White icon for better contrast
                    modifier = Modifier.size(18.dp) // Slightly smaller icon within the circle
                )
            }
        }

    }
}



// Rounded Square Shape (fills the entire bounding box)
fun roundedSquareShape() = GenericShape { size, _ ->
    val cornerRadius = size.width * 0.2f
    addRoundRect(
        androidx.compose.ui.geometry.RoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = cornerRadius,
            radiusY = cornerRadius
        )
    )
}

// Rounded Triangle Shape that occupies the entire bounding box
fun roundedTriangleShape() = GenericShape { size, _ ->
    moveTo(size.width / 2, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

fun amoebaSquareShape() = GenericShape { size, _ ->
    moveTo(size.width * 0.3f, size.height * 0.2f)
    cubicTo(
        size.width * 0.6f, size.height * 0.0f,
        size.width * 0.8f, size.height * 0.3f,
        size.width * 0.7f, size.height * 0.5f
    )
    cubicTo(
        size.width * 1.0f, size.height * 0.8f,
        size.width * 0.5f, size.height * 1.0f,
        size.width * 0.4f, size.height * 0.9f
    )
    cubicTo(
        size.width * 0.0f, size.height * 0.6f,
        size.width * 0.2f, size.height * 0.3f,
        size.width * 0.3f, size.height * 0.2f
    )
    close()
}


// Rounded Star Shape that occupies the entire bounding box
fun roundedStarShape() = GenericShape { size, _ ->
    val outerRadius = size.width / 2
    val innerRadius = outerRadius / 2.5f
    val centerX = size.width / 2
    val centerY = size.height / 2

    for (i in 0 until 5) {
        val angleOuter = Math.toRadians((i * 72).toDouble() - 90)
        val angleInner = Math.toRadians((i * 72 + 36).toDouble() - 90)

        if (i == 0) {
            moveTo(
                (centerX + outerRadius * cos(angleOuter)).toFloat(),
                (centerY + outerRadius * sin(angleOuter)).toFloat()
            )
        } else {
            lineTo(
                (centerX + outerRadius * cos(angleOuter)).toFloat(),
                (centerY + outerRadius * sin(angleOuter)).toFloat()
            )
        }

        lineTo(
            (centerX + innerRadius * cos(angleInner)).toFloat(),
            (centerY + innerRadius * sin(angleInner)).toFloat()
        )
    }
    close()
}

// Rounded Hexagon Shape that occupies the entire bounding box
fun roundedHexagonShape() = GenericShape { size, _ ->
    val scaleFactor = 1.1f // Scale factor to make the hexagon appear larger
    val radius = (size.width / 2) * scaleFactor
    val centerX = size.width / 2
    val centerY = size.height / 2

    moveTo((centerX + radius * cos(0.0)).toFloat(), (centerY + radius * sin(0.0)).toFloat())
    for (i in 1..6) {
        lineTo(
            (centerX + radius * cos(i * Math.PI / 3)).toFloat(),
            (centerY + radius * sin(i * Math.PI / 3)).toFloat()
        )
    }
    close()
}

private fun cos(angle: Double) = Math.cos(angle).toFloat()

private fun sin(angle: Double) = Math.sin(angle).toFloat()

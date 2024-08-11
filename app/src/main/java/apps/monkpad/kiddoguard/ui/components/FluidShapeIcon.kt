package apps.monkpad.kiddoguard.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import apps.monkpad.kiddoguard.ui.theme.LightGray
import apps.monkpad.kiddoguard.ui.theme.Peach
import apps.monkpad.kiddoguard.ui.theme.SkyBlue
import apps.monkpad.kiddoguard.utils.drawableToImageBitmap
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FluidShapeIcon(
    appIcon: Drawable,
    index: Int,
    onClick: () -> Unit // Callback for clicking the icon
) {
    // Define a list of educational shapes with equal width and height
    val shapes = listOf(
        roundedSquareShape(),
//        roundedTriangleShape(),
        roundedHexagonShape()
    )

    // Select a shape based on the index, cycling through the list
    val selectedShape = shapes[index % shapes.size]

    // Gradient color inspired by soft, pastel tones for a sticker effect
    val gradient = Brush.linearGradient(
        colors = listOf(
            Peach,
            LightGray,
            SkyBlue
        )
    )

    Surface(
        modifier = Modifier
            .size(90.dp) // Adjust size as needed
            .padding(4.dp)
            .clickable { onClick() }, // Make the icon clickable
        shape = selectedShape,
        shadowElevation = 4.dp, // Slight elevation for sticker effect
        color = Color.Transparent // Background will be set by the gradient
    ) {
        Box(
            modifier = Modifier
                .background(brush = gradient)
                .border(6.dp, Color.White, selectedShape) // White border for sticker effect
                .padding(12.dp), // Padding inside the sticker shape
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = drawableToImageBitmap(appIcon),
                contentDescription = null,
                modifier = Modifier
                    .size(46.dp) // Icon size
            )
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
    val radius = size.width / 2
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

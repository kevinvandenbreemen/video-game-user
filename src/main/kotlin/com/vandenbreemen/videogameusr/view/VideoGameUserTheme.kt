package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val robotoFontFamily = FontFamily(Font("/Roboto-Regular.ttf"))

val RobotoTypography = Typography(
    h1 = TextStyle(fontWeight = FontWeight.W900, fontSize = 96.sp),
    h2 = TextStyle(fontWeight = FontWeight.W900, fontSize = 60.sp),
    h3 = TextStyle(fontWeight = FontWeight.W900, fontSize = 48.sp),
    h4 = TextStyle(fontWeight = FontWeight.W700, fontSize = 34.sp),
    h5 = TextStyle(fontWeight = FontWeight.W700, fontSize = 24.sp),
    h6 = TextStyle(fontWeight = FontWeight.W700, fontSize = 20.sp),
    subtitle1 = TextStyle(fontWeight = FontWeight.W500, fontSize = 16.sp),
    subtitle2 = TextStyle(fontWeight = FontWeight.W500, fontSize = 14.sp),
    body1 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
    body2 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    button = TextStyle(fontWeight = FontWeight.W700, fontSize = 8.sp),
    caption = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp),
    overline = TextStyle(fontWeight = FontWeight.W500, fontSize = 10.sp)
)


val VideoGameUserTypography = RobotoTypography

private val DarkColorPalette = darkColors(
    primary = Color(0xFF606060),
    primaryVariant = Color(0xFF404040),
    secondary = Color(0xFF808080),
    background = Color(0xFF303030),
    surface = Color(0xFF505050),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Color(0xFFD3D3D3),  // Light Gray
    primaryVariant = Color(0xFFA9A9A9),  // Dark Gray
    secondary = Color(0xFFAA8BC4),  // Yellow
    background = Color(0xFFF5F5F5),  // White Smoke
    surface = Color(0xFFBEBEBE),  // Gray
    onPrimary = Color.Black,
    onSecondary = Color(0xFFFBE3F2),
    onBackground = Color.Black,
    onSurface = Color.Black,
)

object Dimensions {
    val padding = 10.dp
    val borderPadding = 5.dp
    val elevation = 5.dp
}

@Composable
fun VideoGameUserTheme(darkTheme: Boolean = true, content: @Composable() () -> Unit) {
    if(darkTheme) {
        androidx.compose.material.MaterialTheme(
            colors = DarkColorPalette,
            typography = VideoGameUserTypography,
            content = content
        )
    }
    else {
        androidx.compose.material.MaterialTheme(
            colors = LightColorPalette,
            typography = VideoGameUserTypography,
            content = content
        )
    }
}
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
    //  CABBD0
    primary = Color(0xFFAA8BC4),
    primaryVariant = Color(0xFFC189D0),
    secondary = Color(0xFFCABBD0),
    background = Color(0xFF92777F),
    surface = Color(0xFFAF9198),
    onPrimary = Color(0xFFFAE3D6),
    onSecondary = Color(0xFF9E7CCA),
    onBackground = Color(0xFFFBE2F2),
    onSurface = Color(0xFFFAE3D6),
)

private val LightColorPalette = lightColors(
    primary = Color(0xFFD3D3D3),  // Light Gray
    primaryVariant = Color(0xFFA9A9A9),  // Dark Gray
    secondary = Color(0xFFFFFF00),  // Yellow
    background = Color(0xFFF5F5F5),  // White Smoke
    surface = Color(0xFFBEBEBE),  // Gray
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

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
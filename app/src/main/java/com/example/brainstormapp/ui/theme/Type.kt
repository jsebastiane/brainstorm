package com.example.brainstormapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.brainstormapp.R

val archivo = FontFamily(
    listOf(
        Font(
            R.font.archivo_regular, FontWeight.Normal
        ),
        Font(
            R.font.archivo_semi_bold, FontWeight.SemiBold
        ),
        Font(
            R.font.archivo_bold, FontWeight.Bold
        )
    )
)

// Set of Material typography styles to start with
val Typography = Typography(

    titleSmall = TextStyle(
        fontFamily = archivo,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp),

    titleMedium = TextStyle(
        fontFamily = archivo,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = archivo,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)


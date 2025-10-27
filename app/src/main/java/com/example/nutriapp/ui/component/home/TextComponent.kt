package com.example.nutriapp.ui.component.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyText(text: String = "",
           color: Color = Color.Unspecified,
           @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
           textAlign : TextAlign? = null,
           fontStyle: FontStyle? = null,
           fontSize: TextUnit = 12.sp,
           pad: Dp = 0.dp
){ Text(text = text,
    color = color,
    modifier =  modifier.padding(top = pad ),
    fontStyle=fontStyle,
    textAlign = textAlign ,
    fontSize = fontSize)
}
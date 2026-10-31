package com.example.ui.components

import android.view.KeyEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    accentColor: Color = Color(0xFF00E5FF),
    isCardFocused: Boolean = false,
    focusRequester: FocusRequester? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onFocusStateChange: ((Boolean) -> Unit)? = null,
    elevation: Dp = 8.dp,
    content: @Composable BoxScope.(isFocused: Boolean) -> Unit
) {
    var hasFocus by remember { mutableStateOf(false) }
    val effectiveFocus = isCardFocused || hasFocus

    // Smooth fluid scale animation on D-pad focus
    val scale by animateFloatAsState(
        targetValue = if (effectiveFocus) 1.08f else 1.0f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
        label = "glassCardScale"
    )

    // Animated border color reflecting user's chosen accentColor on focus
    val animatedBorderColor by animateColorAsState(
        targetValue = if (effectiveFocus) accentColor else Color.White.copy(alpha = 0.28f),
        label = "glassCardBorder"
    )

    val animatedBorderWidth = if (effectiveFocus) 2.5.dp else 1.2.dp

    // Translucent liquid glass gradient fill
    val glassFillBrush = if (effectiveFocus) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.38f),
                Color.White.copy(alpha = 0.18f),
                Color.White.copy(alpha = 0.28f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.20f),
                Color.White.copy(alpha = 0.08f),
                Color.White.copy(alpha = 0.14f)
            )
        )
    }

    val shadowElevation = if (effectiveFocus) elevation * 2 else elevation

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = shadowElevation,
                shape = shape,
                ambientColor = if (effectiveFocus) accentColor.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.4f),
                spotColor = if (effectiveFocus) accentColor else Color.Black.copy(alpha = 0.5f)
            )
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
            .onFocusChanged { state ->
                hasFocus = state.isFocused
                onFocusStateChange?.invoke(state.isFocused)
            }
            .focusable(enabled = true, interactionSource = interactionSource)
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (keyEvent.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER,
                        KeyEvent.KEYCODE_ENTER,
                        KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                            onClick?.invoke()
                            true
                        }
                        KeyEvent.KEYCODE_MENU -> {
                            onLongClick?.invoke()
                            true
                        }
                        else -> false
                    }
                } else false
            }
            .clip(shape)
            .background(glassFillBrush)
            .border(
                BorderStroke(animatedBorderWidth, animatedBorderColor),
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = accentColor),
                onClick = { onClick?.invoke() }
            )
    ) {
        // Liquid glass top highlight reflection
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (effectiveFocus) 0.32f else 0.18f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = 70f
                    )
                )
        )

        content(effectiveFocus)
    }
}

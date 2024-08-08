package eu.glitch.mywarehouse.ui.elements

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modifier.shake(): Modifier = composed {
    val shakeKeyframes: AnimationSpec<Float> = keyframes {
        durationMillis = 800
        val easing = FastOutLinearInEasing
        // generate 8 keyframes
        for (i in 1..8) {
            val x = when (i % 3) {
                0 -> 4f
                1 -> -4f
                else -> 0f
            }
            x at durationMillis / 10 * i with easing
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            coroutineScope.launch {
                offsetX.animateTo(targetValue = 0f, animationSpec = shakeKeyframes)
            }
            delay(1000)
        }
    }

    this.then(Modifier.offset(offsetX.value.dp, 0.dp))
}
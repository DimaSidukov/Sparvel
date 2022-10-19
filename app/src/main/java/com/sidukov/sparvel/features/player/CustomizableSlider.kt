package com.sidukov.sparvel.features.player

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.*
import com.sidukov.sparvel.features.player.ColorSchemeKeyTokens.*
import com.sidukov.sparvel.features.player.ElevationTokens.Level0
import com.sidukov.sparvel.features.player.ElevationTokens.Level1
import com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerFull
import com.sidukov.sparvel.features.player.SliderTokens.HandleHeight
import com.sidukov.sparvel.features.player.SliderTokens.HandleWidth
import com.sidukov.sparvel.features.player.SliderTokens.InactiveTrackHeight
import com.sidukov.sparvel.features.player.SliderTokens.TickMarksContainerSize
import com.sidukov.sparvel.features.player.SwitchTokens.HandleShape
import com.sidukov.sparvel.features.player.TypographyKeyTokens.LabelMedium
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private val ThumbWidth = HandleWidth
private val ThumbHeight = HandleHeight
private val ThumbSize = DpSize(ThumbWidth, ThumbHeight)
private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp
private val TickSize = TickMarksContainerSize
val ThumbDiameter = SwitchTokens.SelectedHandleWidth
private val SliderHeight = 48.dp
private val SliderMinWidth = 144.dp
val TrackHeight = InactiveTrackHeight
private val DefaultSliderConstraints =
    Modifier
        .widthIn(min = SliderMinWidth)
        .heightIn(max = SliderHeight)

@Composable
fun CustomizableSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    trackHeight: Dp = 6.dp,
    thumbRadius: Dp = 12.dp
) {
    val onValueChangeState = rememberUpdatedState<(Float) -> Unit> {
        if (it != value) {
            onValueChange(it)
        }
    }

    val tickFractions = remember(steps) {
        stepsToTickFractions(steps)
    }
    BoxWithConstraints(
        modifier
            .minimumTouchTargetSize()
            .requiredSizeIn(
                minWidth = SliderTokens.HandleWidth,
                minHeight = SliderTokens.HandleHeight
            )
            .sliderSemantics(
                value,
                enabled,
                onValueChange,
                onValueChangeFinished,
                valueRange,
                steps
            )
            .focusable(enabled, interactionSource)
    ) {
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        val widthPx = constraints.maxWidth.toFloat()
        val maxPx: Float
        val minPx: Float

        with(LocalDensity.current) {
            maxPx = max(widthPx - thumbRadius.toPx(), 0f)
            minPx = min(thumbRadius.toPx(), maxPx)
        }

        fun scaleToUserValue(offset: Float) =
            scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

        fun scaleToOffset(userValue: Float) =
            scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)

        val rawOffset = remember { mutableStateOf(scaleToOffset(value)) }
        val pressOffset = remember { mutableStateOf(0f) }

        val draggableState = remember(minPx, maxPx, valueRange) {
            SliderDraggableState {
                rawOffset.value = (rawOffset.value + it + pressOffset.value)
                pressOffset.value = 0f
                val offsetInTrack = snapValueToTick(rawOffset.value, tickFractions, minPx, maxPx)
                onValueChangeState.value.invoke(scaleToUserValue(offsetInTrack))
            }
        }

        val gestureEndAction = rememberUpdatedState {
            if (!draggableState.isDragging) {
                // check isDragging in case the change is still in progress (touch -> drag case)
                onValueChangeFinished?.invoke()
            }
        }

        val press = Modifier.sliderTapModifier(
            draggableState,
            interactionSource,
            widthPx,
            isRtl,
            rawOffset,
            gestureEndAction,
            pressOffset,
            enabled
        )

        val drag = Modifier.draggable(
            orientation = Orientation.Horizontal,
            reverseDirection = isRtl,
            enabled = enabled,
            interactionSource = interactionSource,
            onDragStopped = { _ -> gestureEndAction.value.invoke() },
            startDragImmediately = draggableState.isDragging,
            state = draggableState
        )

        val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
        val positionFraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)

        val width = maxPx - minPx
        val widthDp: Dp
        val trackStrokeWidth: Float

        with(LocalDensity.current) {
            widthDp = width.toDp()
            trackStrokeWidth = trackHeight.toPx()
        }

        val offset = widthDp * positionFraction

        SliderLayout(
            track = {
                Track(
                    Modifier.fillMaxSize(),
                    colors,
                    enabled,
                    0f,
                    positionFraction,
                    tickFractions,
                    thumbRadius * 2,
                    trackStrokeWidth
                )
            },
            thumb = {
                SliderThumb(
                    Modifier,
                    offset,
                    interactionSource,
                    colors,
                    enabled,
                    DpSize(thumbRadius, thumbRadius)
                )
            },
            modifier = press.then(drag)
        )
    }
}

private fun stepsToTickFractions(steps: Int): List<Float> {
    return if (steps == 0) emptyList() else List(steps + 2) { it.toFloat() / (steps + 1) }
}

private fun snapValueToTick(
    current: Float,
    tickFractions: List<Float>,
    minPx: Float,
    maxPx: Float
): Float {
    // target is a closest anchor to the `current`, if exists
    return tickFractions
        .minByOrNull { abs(lerp(minPx, maxPx, it) - current) }
        ?.run { lerp(minPx, maxPx, this) }
        ?: current
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ModifierInspectorInfo")
internal fun Modifier.minimumTouchTargetSize(): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "minimumTouchTargetSize"
        // TODO: b/214589635 - surface this information through the layout inspector in a better way
        //  - for now just add some information to help developers debug what this size represents.
        properties["README"] = "Adds outer padding to measure at least 48.dp (default) in " +
                "size to disambiguate touch interactions if the element would measure smaller"
    }
) {
    if (LocalMinimumTouchTargetEnforcement.current) {
        // TODO: consider using a hardcoded value of 48.dp instead to avoid inconsistent UI if the
        // LocalViewConfiguration changes across devices / during runtime.
        val size = LocalViewConfiguration.current.minimumTouchTargetSize
        MinimumTouchTargetModifier(size)
    } else {
        Modifier
    }
}

private fun Modifier.sliderSemantics(
    value: Float,
    enabled: Boolean,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
): Modifier {
    val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
    return semantics {
        if (!enabled) disabled()
        setProgress(
            action = { targetValue ->
                var newValue = targetValue.coerceIn(valueRange.start, valueRange.endInclusive)
                val originalVal = newValue
                val resolvedValue = if (steps > 0) {
                    var distance: Float = newValue
                    for (i in 0..steps + 1) {
                        val stepValue = lerp(
                            valueRange.start,
                            valueRange.endInclusive,
                            i.toFloat() / (steps + 1)
                        )
                        if (abs(stepValue - originalVal) <= distance) {
                            distance = abs(stepValue - originalVal)
                            newValue = stepValue
                        }
                    }
                    newValue
                } else {
                    newValue
                }

                // This is to keep it consistent with AbsSeekbar.java: return false if no
                // change from current.
                if (resolvedValue == coerced) {
                    false
                } else {
                    onValueChange(resolvedValue)
                    onValueChangeFinished?.invoke()
                    true
                }
            }
        )
    }.progressSemantics(value, valueRange, steps)
}

private fun Modifier.sliderTapModifier(
    draggableState: DraggableState,
    interactionSource: MutableInteractionSource,
    maxPx: Float,
    isRtl: Boolean,
    rawOffset: State<Float>,
    gestureEndAction: State<() -> Unit>,
    pressOffset: MutableState<Float>,
    enabled: Boolean
) = composed(
    factory = {
        if (enabled) {
            val scope = rememberCoroutineScope()
            pointerInput(draggableState, interactionSource, maxPx, isRtl) {
                detectTapGestures(
                    onPress = { pos ->
                        val to = if (isRtl) maxPx - pos.x else pos.x
                        pressOffset.value = to - rawOffset.value
                        try {
                            awaitRelease()
                        } catch (_: GestureCancellationException) {
                            pressOffset.value = 0f
                        }
                    },
                    onTap = {
                        scope.launch {
                            draggableState.drag(MutatePriority.UserInput) {
                                // just trigger animation, press offset will be applied
                                dragBy(0f)
                            }
                            gestureEndAction.value.invoke()
                        }
                    }
                )
            }
        } else {
            this
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "sliderTapModifier"
        properties["draggableState"] = draggableState
        properties["interactionSource"] = interactionSource
        properties["maxPx"] = maxPx
        properties["isRtl"] = isRtl
        properties["rawOffset"] = rawOffset
        properties["gestureEndAction"] = gestureEndAction
        properties["pressOffset"] = pressOffset
        properties["enabled"] = enabled
    })

@Composable
private fun SliderLayout(
    modifier: Modifier,
    thumb: @Composable BoxScope.() -> Unit,
    track: @Composable () -> Unit,
) {
    Box(modifier.then(DefaultSliderConstraints)) {
        track()
        thumb()
    }
}

@Composable
private fun Track(
    modifier: Modifier,
    colors: SliderColors,
    enabled: Boolean,
    positionFractionStart: Float,
    positionFractionEnd: Float,
    tickFractions: List<Float>,
    thumbWidth: Dp,
    trackStrokeWidth: Float
) {
    val thumbRadiusPx: Float
    val tickSize: Float
    with(LocalDensity.current) {
        thumbRadiusPx = thumbWidth.toPx() / 2
        tickSize = TickSize.toPx()
    }
    val inactiveTrackColor = colors.trackColor(enabled, active = false)
    val activeTrackColor = colors.trackColor(enabled, active = true)
    val inactiveTickColor = colors.tickColor(enabled, active = false)
    val activeTickColor = colors.tickColor(enabled, active = true)
    Canvas(modifier) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(thumbRadiusPx, center.y)
        val sliderRight = Offset(size.width - thumbRadiusPx, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight
        drawLine(
            inactiveTrackColor.value,
            sliderStart,
            sliderEnd,
            trackStrokeWidth,
            StrokeCap.Round
        )
        val sliderValueEnd = Offset(
            sliderStart.x + (sliderEnd.x - sliderStart.x) * positionFractionEnd,
            center.y
        )

        val sliderValueStart = Offset(
            sliderStart.x + (sliderEnd.x - sliderStart.x) * positionFractionStart,
            center.y
        )

        drawLine(
            activeTrackColor.value,
            sliderValueStart,
            sliderValueEnd,
            trackStrokeWidth,
            StrokeCap.Round
        )
        tickFractions.groupBy { it > positionFractionEnd || it < positionFractionStart }
            .forEach { (outsideFraction, list) ->
                drawPoints(
                    list.map {
                        Offset(
                            androidx.compose.ui.geometry.lerp(sliderStart, sliderEnd, it).x,
                            center.y
                        )
                    },
                    PointMode.Points,
                    (if (outsideFraction) inactiveTickColor else activeTickColor).value,
                    tickSize,
                    StrokeCap.Round
                )
            }
    }
}

@Composable
private fun BoxScope.SliderThumb(
    modifier: Modifier,
    offset: Dp,
    interactionSource: MutableInteractionSource,
    colors: SliderColors,
    enabled: Boolean,
    thumbSize: DpSize
) {
    Box(
        Modifier
            .padding(start = offset)
            .align(Alignment.CenterStart)
    ) {
        val interactions = remember { mutableStateListOf<Interaction>() }
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> interactions.add(interaction)
                    is PressInteraction.Release -> interactions.remove(interaction.press)
                    is PressInteraction.Cancel -> interactions.remove(interaction.press)
                    is DragInteraction.Start -> interactions.add(interaction)
                    is DragInteraction.Stop -> interactions.remove(interaction.start)
                    is DragInteraction.Cancel -> interactions.remove(interaction.start)
                }
            }
        }

        val elevation = if (interactions.isNotEmpty()) {
            ThumbPressedElevation
        } else {
            ThumbDefaultElevation
        }
        val shape = HandleShape.toShape()
        Spacer(
            modifier
                .size(thumbSize)
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        bounded = false,
                        radius = SliderTokens.StateLayerSize / 2
                    )
                )
                .hoverable(interactionSource = interactionSource)
                .shadow(if (enabled) elevation else 0.dp, shape, clip = false)
                .background(colors.thumbColor(enabled).value, shape)
        )
    }
}

@Composable
internal fun com.sidukov.sparvel.features.player.ShapeKeyTokens.toShape(): Shape {
    return MaterialTheme.shapes.fromToken(this)
}

fun Shapes.fromToken(value: com.sidukov.sparvel.features.player.ShapeKeyTokens): Shape {
    return when (value) {
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerExtraLarge -> extraLarge
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerExtraLargeTop -> extraLarge.top()
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerExtraSmall -> extraSmall
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerExtraSmallTop -> extraSmall.top()
        CornerFull -> CircleShape
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerLarge -> large
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerLargeEnd -> large.end()
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerLargeTop -> large.top()
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerMedium -> medium
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerNone -> RectangleShape
        com.sidukov.sparvel.features.player.ShapeKeyTokens.CornerSmall -> small
    }
}

fun CornerBasedShape.top(): CornerBasedShape {
    return copy(bottomStart = CornerSize(0.0.dp), bottomEnd = CornerSize(0.0.dp))
}

fun CornerBasedShape.end(): CornerBasedShape {
    return copy(topStart = CornerSize(0.0.dp), bottomStart = CornerSize(0.0.dp))
}


private fun lerp(a: Float, b: Float, fraction: Float): Float =
    a * (1 - fraction) + b * fraction

// Scale x1 from a1..b1 range to a2..b2 range
private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
    lerp(a2, b2, calcFraction(a1, b1, x1))

// Scale x.start, x.endInclusive from a1..b1 range to a2..b2 range
private fun scale(a1: Float, b1: Float, x: ClosedFloatingPointRange<Float>, a2: Float, b2: Float) =
    scale(a1, b1, x.start, a2, b2)..scale(a1, b1, x.endInclusive, a2, b2)

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

private class MinimumTouchTargetModifier(val size: DpSize) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val placeable = measurable.measure(constraints)

        // Be at least as big as the minimum dimension in both dimensions
        val width = maxOf(placeable.width, size.width.roundToPx())
        val height = maxOf(placeable.height, size.height.roundToPx())

        return layout(width, height) {
            val centerX = ((width - placeable.width) / 2f).roundToInt()
            val centerY = ((height - placeable.height) / 2f).roundToInt()
            placeable.place(centerX, centerY)
        }
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? MinimumTouchTargetModifier ?: return false
        return size == otherModifier.size
    }

    override fun hashCode(): Int {
        return size.hashCode()
    }
}

private class SliderDraggableState(
    val onDelta: (Float) -> Unit
) : DraggableState {

    var isDragging by mutableStateOf(false)
        private set

    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = onDelta(pixels)
    }

    private val scrollMutex = MutatorMutex()

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    override fun dispatchRawDelta(delta: Float) {
        return onDelta(delta)
    }
}

object SliderTokens {
    val ActiveTrackColor = Primary
    val ActiveTrackHeight = 6.0.dp
    val ActiveTrackShape = CornerFull
    val DisabledActiveTrackColor = OnSurface
    const val DisabledActiveTrackOpacity = 0.38f
    val DisabledHandleColor = OnSurface
    val DisabledHandleElevation = Level0
    const val DisabledHandleOpacity = 0.38f
    val DisabledInactiveTrackColor = OnSurface
    const val DisabledInactiveTrackOpacity = 0.12f
    val FocusHandleColor = Primary
    val HandleColor = Primary
    val HandleElevation = Level1
    val HandleHeight = 20.0.dp
    val HandleShape = CornerFull
    val HandleWidth = 20.0.dp
    val HoverHandleColor = Primary
    val InactiveTrackColor = SurfaceVariant
    val InactiveTrackHeight = 4.0.dp
    val InactiveTrackShape = CornerFull
    val LabelContainerColor = Primary
    val LabelContainerElevation = Level0
    val LabelContainerHeight = 28.0.dp
    val LabelTextColor = OnPrimary
    val LabelTextFont = LabelMedium
    val PressedHandleColor = Primary
    val StateLayerSize = 40.0.dp
    val TrackElevation = Level0
    val OverlapHandleOutlineColor = OnPrimary
    val OverlapHandleOutlineWidth = 1.0.dp
    val TickMarksActiveContainerColor = OnPrimary
    const val TickMarksActiveContainerOpacity = 0.38f
    val TickMarksContainerShape = CornerFull
    val TickMarksContainerSize = 2.0.dp
    val TickMarksDisabledContainerColor = OnSurface
    const val TickMarksDisabledContainerOpacity = 0.38f
    val TickMarksInactiveContainerColor = OnSurfaceVariant
    const val TickMarksInactiveContainerOpacity = 0.38f
}

object SwitchTokens {
    val DisabledSelectedHandleColor = Surface
    const val DisabledSelectedHandleOpacity = 1.0f
    val DisabledSelectedIconColor = OnSurface
    const val DisabledSelectedIconOpacity = 0.38f
    val DisabledSelectedTrackColor = OnSurface
    const val DisabledTrackOpacity = 0.12f
    val DisabledUnselectedHandleColor = OnSurface
    const val DisabledUnselectedHandleOpacity = 0.38f
    val DisabledUnselectedIconColor = SurfaceVariant
    const val DisabledUnselectedIconOpacity = 0.38f
    val DisabledUnselectedTrackColor = SurfaceVariant
    val DisabledUnselectedTrackOutlineColor = OnSurface
    val HandleShape = CornerFull
    val PressedHandleHeight = 28.0.dp
    val PressedHandleWidth = 28.0.dp
    val SelectedFocusHandleColor = PrimaryContainer
    val SelectedFocusIconColor = OnPrimaryContainer
    val SelectedFocusTrackColor = Primary
    val SelectedHandleColor = OnPrimary
    val SelectedHandleHeight = 24.0.dp
    val SelectedHandleWidth = 24.0.dp
    val SelectedHoverHandleColor = PrimaryContainer
    val SelectedHoverIconColor = OnPrimaryContainer
    val SelectedHoverTrackColor = Primary
    val SelectedIconColor = OnPrimaryContainer
    val SelectedIconSize = 16.0.dp
    val SelectedPressedHandleColor = PrimaryContainer
    val SelectedPressedIconColor = OnPrimaryContainer
    val SelectedPressedTrackColor = Primary
    val SelectedTrackColor = Primary
    val StateLayerShape = CornerFull
    val StateLayerSize = 40.0.dp
    val TrackHeight = 32.0.dp
    val TrackOutlineWidth = 2.0.dp
    val TrackShape = CornerFull
    val TrackWidth = 52.0.dp
    val UnselectedFocusHandleColor = OnSurfaceVariant
    val UnselectedFocusIconColor = SurfaceVariant
    val UnselectedFocusTrackColor = SurfaceVariant
    val UnselectedFocusTrackOutlineColor = Outline
    val UnselectedHandleColor = Outline
    val UnselectedHandleHeight = 16.0.dp
    val UnselectedHandleWidth = 16.0.dp
    val UnselectedHoverHandleColor = OnSurfaceVariant
    val UnselectedHoverIconColor = SurfaceVariant
    val UnselectedHoverTrackColor = SurfaceVariant
    val UnselectedHoverTrackOutlineColor = Outline
    val UnselectedIconColor = SurfaceVariant
    val UnselectedIconSize = 16.0.dp
    val UnselectedPressedHandleColor = OnSurfaceVariant
    val UnselectedPressedIconColor = SurfaceVariant
    val UnselectedPressedTrackColor = SurfaceVariant
    val UnselectedPressedTrackOutlineColor = Outline
    val UnselectedTrackColor = SurfaceVariant
    val IconHandleHeight = 24.0.dp
    val IconHandleWidth = 24.0.dp
}

enum class ColorSchemeKeyTokens {
    Background,
    Error,
    ErrorContainer,
    InverseOnSurface,
    InversePrimary,
    InverseSurface,
    OnBackground,
    OnError,
    OnErrorContainer,
    OnPrimary,
    OnPrimaryContainer,
    OnSecondary,
    OnSecondaryContainer,
    OnSurface,
    OnSurfaceVariant,
    OnTertiary,
    OnTertiaryContainer,
    Outline,
    OutlineVariant,
    Primary,
    PrimaryContainer,
    Scrim,
    Secondary,
    SecondaryContainer,
    Surface,
    SurfaceTint,
    SurfaceVariant,
    Tertiary,
    TertiaryContainer,
}

enum class ShapeKeyTokens {
    CornerExtraLarge,
    CornerExtraLargeTop,
    CornerExtraSmall,
    CornerExtraSmallTop,
    CornerFull,
    CornerLarge,
    CornerLargeEnd,
    CornerLargeTop,
    CornerMedium,
    CornerNone,
    CornerSmall,
}

object ElevationTokens {
    val Level0 = 0.0.dp
    val Level1 = 1.0.dp
    val Level2 = 3.0.dp
    val Level3 = 6.0.dp
    val Level4 = 8.0.dp
    val Level5 = 12.0.dp
}

enum class TypographyKeyTokens {
    BodyLarge,
    BodyMedium,
    BodySmall,
    DisplayLarge,
    DisplayMedium,
    DisplaySmall,
    HeadlineLarge,
    HeadlineMedium,
    HeadlineSmall,
    LabelLarge,
    LabelMedium,
    LabelSmall,
    TitleLarge,
    TitleMedium,
    TitleSmall,
}
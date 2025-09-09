package fr.o80.algorithms.entityrelationship.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import fr.o80.algorithms.entityrelationship.domain.GetPlacedNodes

@Composable
fun EntityRelationshipScreen(
    modifier: Modifier = Modifier,
) {
    val getPlacedNodes = remember { GetPlacedNodes() }
    val placedNodes = remember { getPlacedNodes() }
    val width = remember { placedNodes.maxOf { it.x } - placedNodes.minOf { it.x } }
    val height = remember { placedNodes.maxOf { it.y } - placedNodes.minOf { it.y } }

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
        ) {
            // (size.width / 2f, 0f)
            translate(
                left = size.width / 2f,
                top = (size.height / 2f) - (height * size.height / 2f),
            ) {
                placedNodes.forEach { placedNode ->
                    val textLayoutResult = textMeasurer.measure(placedNode.node.name)
                    val center = Offset(placedNode.x * size.width, placedNode.y * size.height)

                    drawCircle(
                        color = Color.LightGray,
                        radius = 10f,
                        center = center,
                    )
                    drawText(
                        color = Color.Black,
                        textLayoutResult = textLayoutResult,
                        topLeft = center - Offset(textLayoutResult.size.width / 2f, textLayoutResult.size.height / 2f),
                    )

                    placedNode.node.children
                        .map { child -> placedNodes.first { it.node.name == child.name } }
                        .forEach { child ->
                            drawArrow(
                                start = center,
                                end = Offset(child.x * size.width, child.y * size.height),
                            )
                        }
                }
            }
        }
    }
}

private fun DrawScope.drawArrow(
    start: Offset,
    end: Offset,
    color: Color = Color.White,
    padding: Float = 20f,
) {
    val paddingOffset = (end - start).let { it / it.getDistance() } * padding
    drawLine(
        color = color,
        start = start + paddingOffset,
        end = end - paddingOffset,
    )
    rotate(
        degrees = 20f,
        pivot = end - paddingOffset,
    ) {
        drawLine(
            color = color,
            start = end - paddingOffset - paddingOffset,
            end = end - paddingOffset,
        )
    }
    rotate(
        degrees = -20f,
        pivot = end - paddingOffset,
    ) {
        drawLine(
            color = color,
            start = end - paddingOffset - paddingOffset,
            end = end - paddingOffset,
        )
    }
}

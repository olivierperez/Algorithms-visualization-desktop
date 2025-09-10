package fr.o80.algorithms.entityrelationship.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import fr.o80.algorithms.entityrelationship.domain.PlacedNode

@Composable
fun EntityRelationshipGraph(
    nodes: List<PlacedNode>,
    modifier: Modifier = Modifier,
) {
    val height = remember { nodes.maxOf { it.y } - nodes.minOf { it.y } }
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier,
    ) {
        translate(
            left = size.width / 2f,
            top = (size.height / 2f) - (height * size.height / 2f),
        ) {
            nodes.forEach { placedNode ->
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
                    .map { child -> nodes.first { it.node.name == child.name } }
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

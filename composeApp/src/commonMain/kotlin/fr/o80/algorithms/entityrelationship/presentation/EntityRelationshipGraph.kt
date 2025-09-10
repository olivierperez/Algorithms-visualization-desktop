package fr.o80.algorithms.entityrelationship.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.Layout
import fr.o80.algorithms.entityrelationship.domain.Node
import fr.o80.algorithms.entityrelationship.domain.PlacedNode

@Composable
fun EntityRelationshipGraph(
    nodes: List<PlacedNode>,
    node: @Composable (Node) -> Unit,
    modifier: Modifier = Modifier,
) {
    val xOffset = remember {
        val xMin = nodes.minOf { it.x }
        val xDelta = nodes.maxOf { it.x } - xMin
        -xMin - xDelta / 2 + .5f
    }
    val yOffset = remember {
        val yMin = nodes.minOf { it.y }
        val yDelta = nodes.maxOf { it.y } - yMin
        -yMin - yDelta / 2 + .5f
    }

    Box(modifier = modifier) {
        Layout(
            modifier = Modifier.matchParentSize(),
            measurePolicy = { measurables, constraints ->
                val relaxedConstraints = constraints.copy(minWidth = 0, minHeight = 0)
                val placeables = measurables.map { measurable -> measurable.measure(relaxedConstraints) }
                val width = constraints.maxWidth
                val height = constraints.maxHeight

                layout(width, height) {
                    placeables.forEachIndexed { index, placeable ->
                        val node = nodes[index]
                        val x = (node.x * width - placeable.width / 2 + xOffset * width).toInt()
                        val y = (node.y * height - placeable.height / 2 + yOffset * height).toInt()
                        placeable.place(x, y)
                    }
                }
            },
            content = {
                nodes.forEach { node(it.node) }
            },
        )


        val height = remember { nodes.maxOf { it.y } - nodes.minOf { it.y } }
        Canvas(
            modifier = Modifier.matchParentSize(),
        ) {
            translate(
                left = size.width / 2f,
                top = (size.height / 2f) - (height * size.height / 2f),
            ) {
                nodes.forEach { placedNode ->
                    val center = Offset(placedNode.x * size.width, placedNode.y * size.height)

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

@Preview
@Composable
fun EntityRelationshipGraphPreview() {
    MaterialTheme {
        EntityRelationshipGraph(
            nodes = listOf(
                PlacedNode(Node("A"), 0f, 0f),
                PlacedNode(Node("B"), 1f, 0f),
            ),
            modifier = Modifier.fillMaxSize(),
            node = { node -> Text(text = node.name) },
        )
    }
}

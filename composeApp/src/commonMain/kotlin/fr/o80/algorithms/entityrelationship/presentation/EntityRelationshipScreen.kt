package fr.o80.algorithms.entityrelationship.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.o80.algorithms.entityrelationship.domain.GetPlacedNodes

@Composable
fun EntityRelationshipScreen(
    modifier: Modifier = Modifier,
) {
    val getPlacedNodes = remember { GetPlacedNodes() }
    val placedNodes = remember { getPlacedNodes() }

    EntityRelationshipGraph(
        nodes = placedNodes,
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        node = { node ->
            TextButton(
                onClick = {
                    println("Clicked on ${node.name}")
                },
            ) {
                Text(
                    text = node.name,
                )
            }
        },
    )
}

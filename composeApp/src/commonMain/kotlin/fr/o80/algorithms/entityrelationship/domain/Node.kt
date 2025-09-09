package fr.o80.algorithms.entityrelationship.domain

data class Node(
    val name: String,
    val children: List<Node> = emptyList(),
)

data class PlacedNode(
    val node: Node,
    val x: Float,
    val y: Float,
)

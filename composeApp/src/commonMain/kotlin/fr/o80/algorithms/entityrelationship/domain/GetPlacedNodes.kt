package fr.o80.algorithms.entityrelationship.domain

class GetPlacedNodes{

    operator fun invoke(): List<PlacedNode> {
        val a = Node("A")
        val b = Node("B", listOf(a))
        val c = Node("C", listOf(a))
        val d = Node("D", listOf(b, c))
        val e = Node("E", listOf(a, b, c))
        val f = Node("F", listOf(d))
        val g = Node("G", listOf(a, e))
        val h = Node("H", listOf(d))
        val nodes = listOf(a, b, c, d, e, f, g, h)

        return placeNodes(nodes)
    }

    /**
     * Use Layered graph drawing (Sugiyama et al. 1981) to place nodes.
     */
    private fun placeNodes(nodes: List<Node>): List<PlacedNode> {
        val layers = assignLayers(nodes)
        val orderedLayers = reduceCrossings(layers, nodes)
        return calculatePositions(orderedLayers)
    }

    private fun assignLayers(nodes: List<Node>): Map<Int, List<Node>> {
        val layerMap = mutableMapOf<Node, Int>()
        val visited = mutableSetOf<Node>()

        // Trouver les nœuds sources (sans dépendances)
        val sources = nodes.filter { it.children.isEmpty() }

        // Assigner la couche 0 aux sources
        sources.forEach { layerMap[it] = 0 }

        // Propagation en largeur pour assigner les couches
        val queue = ArrayDeque(sources)
        visited.addAll(sources)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val currentLayer = layerMap[current]!!

            // Trouver les nœuds qui dépendent du nœud actuel
            val dependents = nodes.filter { node ->
                node.children.contains(current)
            }

            dependents.forEach { dependent ->
                if (!visited.contains(dependent)) {
                    // Calculer la couche maximale des dépendances
                    val maxDepLayer = dependent.children.maxOfOrNull { dep ->
                        layerMap[dep] ?: -1
                    } ?: -1

                    layerMap[dependent] = maxDepLayer + 1
                    queue.add(dependent)
                    visited.add(dependent)
                }
            }
        }

        // Grouper par couches
        return layerMap.entries.groupBy({ it.value }, { it.key })
    }

    private fun reduceCrossings(
        layers: Map<Int, List<Node>>,
        allNodes: List<Node>,
    ): Map<Int, List<Node>> {
        val orderedLayers = layers.toMutableMap()
        val maxLayer = layers.keys.maxOrNull() ?: return orderedLayers

        // Algorithme de réduction des croisements (heuristique simple)
        repeat(3) { // Plusieurs passes pour améliorer
            for (layer in 1..maxLayer) {
                val currentLayer = orderedLayers[layer] ?: continue
                val previousLayer = orderedLayers[layer - 1] ?: continue

                // Trier par position moyenne des dépendances dans la couche précédente
                val sorted = currentLayer.sortedBy { node ->
                    val deps = node.children.filter { it in previousLayer }
                    if (deps.isEmpty()) 0.0
                    else deps.map { dep -> previousLayer.indexOf(dep) }.average()
                }

                orderedLayers[layer] = sorted
            }
        }

        return orderedLayers
    }

    private fun calculatePositions(layers: Map<Int, List<Node>>): List<PlacedNode> {
        val result = mutableListOf<PlacedNode>()
        val layerSpacing = 1f / layers.size
        val maxNodesByLayer = layers.map { it.value.size }.max()
        val nodeSpacing = 1f / maxNodesByLayer

        layers.forEach { (layerIndex, nodesInLayer) ->
            val y = layerIndex * layerSpacing
            val totalWidth = (nodesInLayer.size - 1) * nodeSpacing
            val startX = -totalWidth / 2

            nodesInLayer.forEachIndexed { index, node ->
                val x = startX + index * nodeSpacing
                result.add(PlacedNode(node, x.toFloat(), y))
            }
        }

        return result
    }
}

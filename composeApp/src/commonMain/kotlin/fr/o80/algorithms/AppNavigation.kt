package fr.o80.algorithms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.o80.algorithms.entityrelationship.EntityRelationshipRoute
import fr.o80.algorithms.entityrelationship.setupEntityRelationshipNavigation

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = EntityRelationshipRoute,
        modifier = modifier,
    ) {
        context(navController) {
            setupEntityRelationshipNavigation()
        }
    }
}

package fr.o80.algorithms.entityrelationship

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import fr.o80.algorithms.entityrelationship.presentation.EntityRelationshipScreen
import kotlinx.serialization.Serializable

@Serializable
data object EntityRelationshipRoute

context(_: NavController)
fun NavGraphBuilder.setupEntityRelationshipNavigation() {
    composable<EntityRelationshipRoute> {
        EntityRelationshipScreen()
    }
}

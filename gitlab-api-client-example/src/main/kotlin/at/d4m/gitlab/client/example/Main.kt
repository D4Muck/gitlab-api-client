package at.d4m.gitlab.client.example

import at.d4m.gitlab.client.GitlabApiClient
import at.d4m.gitlab.client.Project
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

/**
 * @author Christoph Muck
 */

val ignored = System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
val logger = LoggerFactory.getLogger("Main")

fun main(args: Array<String>) {

    //Replace with your own information
    val client = GitlabApiClient("http://localhost:32769", "ooTd6wm_7h4DGUge85-Y")

    //Get all user profiles accessible to the current user
    logger.info(client.getUserProfiles().collectList().onErrorLogAndComplete().block() ?: "No user profiles")

    //Get all projects accessible to the current user
    logger.info(client.getProjects().collectList().onErrorLogAndComplete().block() ?: "No projects")

    //Create project for the current user
    val project = Mono.just(Project(name = "test-project"))
    val createdProject = client.createProject(project).onErrorLogAndComplete().block()
    logger.info(createdProject ?: "No project created")

    //Remove just created project if it creation was successful
    if (createdProject != null) {
        logger.info(client.removeProject(createdProject.id).onErrorLogAndComplete().block() ?: "Couldn't remove project")
    }
}

fun <T> Mono<T>.onErrorLogAndComplete(): Mono<T> {
    return this.onErrorResume(
            WebClientResponseException::class.java,
            { e ->
                at.d4m.gitlab.client.example.logger.warn(
                        """
                            HTTP Status Error: ${e.rawStatusCode} - ${e.statusText}
                            ${e.responseBodyAsString}
                        """.trimIndent()
                )
                Mono.empty()
            }
    )
}
